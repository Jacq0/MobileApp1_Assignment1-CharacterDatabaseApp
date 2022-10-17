package org.assignments.characterdb.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging

import org.assignments.characterdb.helpers.*
import java.util.*
import java.sql.*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

val dbUser = "root"
val dbPassword = ""
val dbUri = "localhost"
val dbPort = 3306

val dbName = "MobileAppDatabase"
val dbCharacterTable = "Characters"

private val logger = KotlinLogging.logger {}

//These have to be null by default before initialised
var conn : Connection? = null

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
                dbChar.dateTimeAdded=convertToDateTime(results.getDate("ADDED"))
                dbChar.lastModified=convertToDateTime(results.getDate("LAST_MODIFIED"))

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

            var result = statement.executeUpdate("INSERT INTO " + dbCharacterTable + " " +
                    "(ID, NAME, DESCRIPTION, OCCUPATION, ORIGINAL_APPEARANCE, APPEARANCE_YEAR, ADDED, LAST_MODIFIED) VALUES " +
                    "(" + character.id + ", " + character.name + ", " + character.description + ", " + character.occupations + ", " +
                    character.originalAppearance + ", " + character.originalAppearanceYear + ", " + character.dateTimeAdded + ", " + character.lastModified + ")")

            characters.add(character) //add to character list last
        }
        catch(ex: SQLException)
        {
            logger.error { ex.toString() }
        }

    }

    override fun update(character: CharacterModel)
    {
        TODO("Not yet implemented")
    }

    override fun delete(character: CharacterModel)
    {
        TODO("Not yet implemented")
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

    private fun convertToDateTime(inDate: Date): LocalDateTime
    {
        return inDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}