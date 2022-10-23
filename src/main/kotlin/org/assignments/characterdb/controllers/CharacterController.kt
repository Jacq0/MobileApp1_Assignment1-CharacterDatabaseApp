package org.assignments.characterdb.controllers

import mu.KotlinLogging
import org.assignments.characterdb.models.CharacterDBStore
import org.assignments.characterdb.models.CharacterModel
import org.assignments.characterdb.views.CharacterView
import java.sql.SQLException

class CharacterController
{
    val charDB = CharacterDBStore()
    val charView = CharacterView()
    val logger = KotlinLogging.logger {}

    init {
        logger.info { "Launching Character Database App" }
        println("Character Database App v1.0")
    }

    fun start()
    {
        var input: Int

        do
        {
            input = mainMenuReturn()
            when (input) {
                1 -> add()
                2 -> update()
                3 -> delete()
                4 -> listMenu()
                5 -> searchMenu()
                6 -> databaseMenu()
                0 -> println("Exiting App")
                else -> println("Invalid Option")
            }
        }
        while (input != 0)

        logger.info{"Exiting Character Database App"}
    }

    fun mainMenuReturn() : Int
    {
        return charView.menu()
    }

    fun searchMenu()
    {
        var input: Int

        do
        {
            input = searchMenuReturn()
            when (input)
            {
                1 -> searchByName()
                2 -> search()
                0 -> exitSearch()
                else -> println("Invalid Option")
            }
        }
        while (input != 0)
    }

    fun searchMenuReturn(): Int
    {
        return charView.searchMenu()
    }

    fun listMenu()
    {
        var input: Int

        do
        {
            input = listMenuReturn()
            when (input)
            {
                1 -> listAlphabetically()
                2 -> listByYear()
                3 -> println("Listed")
                4 -> println("Listed")
                0 -> exitSearch()
                else -> println("Invalid Option")
            }
        }
        while (input != 0)
    }

    fun listMenuReturn() : Int
    {
        return charView.listMenu()
    }

    fun databaseMenu()
    {
        var input: Int

        do
        {
            input = databaseMenuReturn()
            when (input)
            {
                1 -> exportCharsToJSON()
                2 -> importCharsFromJSON()
                3 -> wipeDatabase()
                4 -> wipeJSON()
                0 -> exitSearch()
                else -> println("Invalid Option")
            }
        }
        while (input != 0)
    }

    fun databaseMenuReturn(): Int
    {
        return charView.databaseMenu()
    }

    fun exitSearch()
    {

    }

    fun add()
    {
        val aCharacter = CharacterModel()

        if (charView.addCharacterData(aCharacter))
        {
            charDB.create(aCharacter)
        }
        else
        {
            logger.info("Character Not Added - Potential Input Error")
        }
    }

    fun list()
    {
        charView.listCharacters(charDB)
    }

    fun listAlphabetically()
    {
        charView.listCharactersAlphabetically(charDB)
    }

    fun listByYear()
    {
        charView.listCharactersByYear(charDB)
    }

    fun update()
    {
        charView.listCharacters(charDB)
        val searchId = charView.getId()
        val aCharacter = search(searchId)

        if(aCharacter != null)
        {
            if(charView.updateCharacterData(aCharacter))
            {
                charDB.update(aCharacter)
                charView.showCharacter(aCharacter)
                logger.info("Character Updated : \n" + charDB.toStringVerbose(aCharacter))
            }
            else
            {
                logger.info("Character Not Updated - Potential Input Error")
            }
        }
        else
        {
            logger.info("A Character was not Found...")
        }
    }

    fun delete()
    {
        charView.listCharacters(charDB)
        var searchId = charView.getId()
        val aCharacter = search(searchId)

        if(aCharacter != null) {
            charDB.delete(aCharacter)
            println("Character Deleted...")
            charView.listCharacters(charDB)
        }
        else
            println("Placemark Not Deleted...")
    }

    fun search()
    {
        val aCharacter = search(charView.getId())!!

        if(aCharacter != null)
        {
            charView.showCharacter(aCharacter)
        }
        else
        {
            println("No character found with this ID")
        }
    }


    fun search(id: Long) : CharacterModel?
    {
        val foundChar = charDB.getOne(id)
        return foundChar
    }

    fun searchByName()
    {
        val chars = searchByName(charView.getName())!!

        if (chars != null && !chars.isEmpty())
        {
            charView.showCharacters(chars)
        }
        else
        {
            println("No Character found with this Name")
        }
    }

    fun searchByName(name: String): List<CharacterModel>?
    {
        val foundChars = charDB.getByName(name)
        return foundChars
    }

    fun importCharsFromJSON()
    {
        charView.loadFromJSON(charDB)
    }

    fun exportCharsToJSON()
    {
        charView.exportToJSON(charDB)
    }

    fun wipeDatabase()
    {
        println("Are you SURE you want to wipe the Database?")
        println("(You can back it up by exporting it to the JSON file)")
        print("Enter \"YES\" to Confirm or anything else to cancel > ")

        var input = readLine()!!

        if(input.uppercase() == "YES")
        {
            try
            {
                charView.wipeDatabase(charDB)

                logger.info("The Database has been wiped.")
            }
            catch(ex: SQLException)
            {
                logger.info("ERROR WITH WIPING: No changes have been made to the Database.")
            }
        }
        else
        {
            logger.info("No changes have been made to the Database.")
        }
    }

    fun wipeJSON()
    {
        println("Are you SURE you want to wipe the JSON file?")
        print("Enter \"YES\" to Confirm or anything else to cancel > ")

        var input = readLine()!!

        if(input.uppercase() == "YES")
        {
            try
            {
                charView.wipeDatabase(charDB)

                logger.info("The file has been wiped.")
            }
            catch(ex: SQLException)
            {
                logger.info("ERROR WITH WIPING: No changes have been made to the File.")
            }
        }
        else
        {
            logger.info("No changes have been made to the File.")
        }
    }
}