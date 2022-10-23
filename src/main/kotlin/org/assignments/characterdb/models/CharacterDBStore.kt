package org.assignments.characterdb.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.server.util.*
import io.ktor.util.*
import mu.KotlinLogging
import org.assignments.characterdb.helpers.exists
import org.assignments.characterdb.helpers.read
import org.assignments.characterdb.helpers.write
import java.sql.*
import java.util.Date
import java.time.LocalDateTime
import java.util.*

val dbUser = "root"
val dbPassword = ""
val dbUri = "localhost"
val dbPort = 3306

val dbName = "MobileAppDatabase"
val dbCharacterTable = "Characters"

private val logger = KotlinLogging.logger {}

//These have to be null by default before initialised
var conn : Connection? = null

//data for the JSON read/write
val JSON_FILE = "characters.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<CharacterModel>>() {}.type

fun generateRandomId(): Long
{
    return Random().nextLong()
}

@OptIn(InternalAPI::class)
class CharacterDBStore: CharacterStore
{
    var characters = mutableListOf<CharacterModel>()

    init
    {
        try
        {
            //first pull in all characters from database
            conn = connectWithDatabase()

            //handle these better just in case they turn null, but the try catch should catch them for now
            var statement= conn!!.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            var results = statement.executeQuery("select * from " + dbCharacterTable)

            //now add all characters to the charaacter list
            while(results.next())
            {
                //create a new character object and add it to the list
                var dbChar = CharacterModel()

                dbChar.id=results.getLong("ID")
                dbChar.name=results.getString("NAME")
                dbChar.description=results.getString("DESCRIPTION")
                dbChar.occupations=results.getString("OCCUPATION")
                dbChar.originalAppearance=results.getString("ORIGINAL_APPEARANCE")
                dbChar.originalAppearanceYear=results.getInt("APPEARANCE_YEAR")
                dbChar.dateTimeAdded=convertToDateTime(results.getDate("ADDED"))!! //this will never be a null value unless something dire happens
                dbChar.lastModified=convertToDateTime(results.getDate("LAST_MODIFIED")) //this might be null if we haven't updated it yet.

                characters.add(dbChar);
            }
        }
        catch (ex: SQLException)
        {
            logger.error { ex.toString() }
        }
    }

    override fun getAll(): List<CharacterModel>
    {
        return characters;
    }

    override fun getOne(id: Long) : CharacterModel?
    {
        var foundChar: CharacterModel? = characters.find { c -> c.id == id }

        return foundChar
    }

    internal fun logAll()
    {
        characters.forEach{
            println(toString(it))
        }
    }

    override fun getAllAlphabeticallyByName()
    {
       var sortedChars = characters.sortedBy { it.name }

        sortedChars.forEach {
            println(toString(it))
        }
    }

    override fun getAllByFirstAppearanceDate()
    {
        var sortedChars = characters.sortedBy { it.originalAppearanceYear }

        sortedChars.forEach {
            println(toString(it))
        }
    }

    override fun getByName(n: String): MutableList<CharacterModel>?
    {
        val chars = mutableListOf<CharacterModel>()

        for(c in characters)
        {
            if(c.name.contains(n, true))
            {
                chars.add(c)
            }
        }

        return chars
    }

    override fun create(character: CharacterModel)
    {
        character.id = generateRandomId() //give a random ID

        //try add to database
        try
        {
            var statement= conn!!.createStatement();

            //we don't need to add the last modified value here because its not applicable to an add. (If its null the character entry was never modified)
            var result = statement.executeUpdate("INSERT INTO " + dbCharacterTable + " " +
                    "(ID, NAME, DESCRIPTION, OCCUPATION, ORIGINAL_APPEARANCE, APPEARANCE_YEAR, ADDED) VALUES " +
                    "(\'" + character.id + "\', \'" + character.name.replace("\'","\'\'") + "\',\'"
                    + character.description.replace("\'","\'\'") + "\', \'" + character.occupations.replace("\'","\'\'") + "\', \'"
                    + character.originalAppearance.replace("\'","\'\'") + "\', \'" + character.originalAppearanceYear + "\', \'" + character.dateTimeAdded + "\')")

            characters.add(character) //add to character list last
        }
        catch(ex: SQLException)
        {
            logger.error { ex.toString() }
        }
    }

    override fun update(character: CharacterModel)
    {
        var foundChar = getOne(character.id!!)
        if (foundChar != null)
        {
            try
            {
                foundChar.name = character.name
                foundChar.description = character.description
                foundChar.occupations = character.occupations
                foundChar.originalAppearance = character.originalAppearance
                foundChar.originalAppearanceYear = character.originalAppearanceYear
                foundChar.lastModified = Date(System.currentTimeMillis()).toLocalDateTime();

                var statement= conn!!.createStatement();

                var result = statement.executeUpdate("UPDATE " + dbCharacterTable + " SET NAME = \'" + foundChar?.name?.replace("\'","\'\'")
                        + "\', DESCRIPTION = \'" + foundChar?.description?.replace("\'","\'\'") + "\'," + "OCCUPATION = \'" + foundChar?.occupations?.replace("\'","\'\'") + "\',"
                        + "ORIGINAl_APPEARANCE = \'" + foundChar?.originalAppearance?.replace("\'","\'\'") + "\'," + "APPEARANCE_YEAR = \'"
                        + foundChar?.originalAppearanceYear + "\'," + "LAST_MODIFIED = \'" + foundChar?.lastModified + "\' WHERE ID = \'" + foundChar?.id + "\'")
            }
            catch(ex: SQLException)
            {
                logger.error { ex.toString() }
            }
        }
        else
        {
            //no char found.
            logger.error{ "Character not found" }
        }
    }

    override fun delete(character: CharacterModel)
    {
        try
        {
            var statement= conn!!.createStatement();

            //we don't need to add the last modified value here because its not applicable to an add. (If its null the character entry was never modified)
            var result = statement.executeUpdate("DELETE FROM " + dbCharacterTable + " WHERE ID = " + "\'" + character.id + "\'");

            characters.remove(character)
        }
        catch(ex: SQLException)
        {
            logger.error { ex.toString() }
        }
    }

    //Import and export from JSON, these are only run when the user wants them to be
    override fun writeToJSON()
    {
        val jsonString = gsonBuilder.toJson(characters, listType)
        write(JSON_FILE, jsonString)
    }

    override fun readFromJSON()
    {
        try
        {
            val jsonString = read(JSON_FILE)

            var inChars : MutableList<CharacterModel> = Gson().fromJson(jsonString, listType)

            for (char in inChars)
            {
                create(char) //create this char in the database
            }
        }
        catch(ex: Exception)
        {
            logger.error { ex.toString() }
        }
    }

    override fun wipeDatabase()
    {
        //try wipe the data from the table
        try
        {
            var statement= conn!!.createStatement();

            //we don't need to add the last modified value here because its not applicable to an add. (If its null the character entry was never modified)
            var result = statement.executeUpdate("DELETE FROM " + dbCharacterTable);

            characters = mutableListOf<CharacterModel>() //set the characters to a new list
        }
        catch(ex: SQLException)
        {
            logger.error { ex.toString() }
        }
    }

    //wipe the json file
    override fun wipeJSON()
    {
        try
        {
            val chars = mutableListOf<CharacterModel>()

            val jsonString = gsonBuilder.toJson(chars, listType)
            write(JSON_FILE, jsonString)

            characters = chars
        }
        catch(ex: Exception)
        {
            logger.error { ex.toString() }
        }
    }

    private fun connectWithDatabase() : Connection
    {
        var properties = Properties()

        properties.put("user", dbUser)
        properties.put("password", dbPassword)

        return DriverManager.getConnection("jdbc:mysql://" + dbUri + ":" + dbPort + "/" + dbName, properties)
    }

    @OptIn(InternalAPI::class) //this is necessary for part of the method to work, no idea why
    private fun convertToDateTime(inDate: Date?): LocalDateTime?
    {
        if(inDate != null)
        {
            val date = Date(System.currentTimeMillis())

            val localDateTime: LocalDateTime = date.toLocalDateTime();

            return localDateTime
        }
        return null;
    }

    //methods for printing the character to string in a nice way.
    private fun toString(char: CharacterModel): String
    {
        val string = "---------------\n" +
            "ID: " + char.id + "\n" + "Name: " + char.name + "\n" +
                "Original Appearance: " + char.originalAppearance + " (" + char.originalAppearanceYear + ")"

        return string
    }

    public fun toStringVerbose(char: CharacterModel): String
    {
        val string = "---------------\n" +
                "ID: " + char.id + "\n" +
                "Name: " + char.name + "\n" +
                "Occupation: " +  char.occupations + "\n" +
                "Original Appearance: " + char.originalAppearance + " (" + char.originalAppearanceYear + ")\n" +
                "\n---Description--- \n" + char.description + "\n" +

                "Added: " + char.dateTimeAdded + "\n" +
                "Last Modified: " + char.lastModified

        return string
    }
}