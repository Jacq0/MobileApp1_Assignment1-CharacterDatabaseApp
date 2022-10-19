package org.assignments.characterdb.models

import io.ktor.server.util.*
import io.ktor.util.*
import mu.KotlinLogging
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
                    "(\'" + character.id + "\', \'" + character.name + "\',\'" + character.description + "\', \'" + character.occupations + "\', \'" +
                    character.originalAppearance + "\', \'" + character.originalAppearanceYear + "\', \'" + character.dateTimeAdded + "\')")

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
            foundChar.name = character.name
            foundChar.description = character.description
            foundChar.occupations = character.occupations
            foundChar.originalAppearance = character.originalAppearance
            foundChar.originalAppearanceYear = character.originalAppearanceYear
            foundChar.lastModified = Date(System.currentTimeMillis()).toLocalDateTime();
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

    internal fun logAll()
    {
        characters.forEach { logger.info("${it}") }
    }

    private fun connectWithDatabase() : Connection
    {
        var properties = Properties()

        properties.put("user", dbUser)
        properties.put("password", dbPassword)

        return DriverManager.getConnection("jdbc:mysql://" + dbUri + ":" + dbPort + "/" + dbName, properties)
    }

    @OptIn(InternalAPI::class)
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
}