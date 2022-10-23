package org.assignments.characterdb.views

import jdk.jfr.Description
import org.assignments.characterdb.models.CharacterDBStore
import org.assignments.characterdb.models.CharacterModel

class CharacterView {

    fun menu(): Int
    {
        var option: Int
        var input: String?

        println("------------------------------------")
        println("Main Menu")
        println("1: Add a Character")
        println("2: Update a Character")
        println("3: Delete a Character")
        println("4: List Characters")
        println("5: Search Characters")
        println("6: Database Options")
        println("0: Exit")
        print("> ")

        input = readLine()!!

        option = if(input.toIntOrNull() != null && !input.isEmpty())
        {
            input.toInt()
        }
        else
        {
            -1
        }

        return option
    }

    fun searchMenu() : Int
    {
        var option: Int
        var input: String?

        println("------------------------------------")
        println("Search Menu")
        println("1: Search by Name")
        println("2: Search by ID")
        println("0: Exit Search Menu")
        print("> ")

        input = readLine()!!

        option = if(input.toIntOrNull() != null && !input.isEmpty())
        {
            input.toInt()
        }
        else
        {
            -1
        }

        return option
    }

    fun listMenu() : Int
    {
        var option: Int
        var input: String?

        println("------------------------------------")
        println("List Menu")
        println("1: List Alphabetically")
        println("2: List by Year of Appearance")
        println("3: List by Date Added")
        println("4: List last Modified")
        println("0: Exit List Menu")
        print("> ")

        input = readLine()!!

        option = if(input.toIntOrNull() != null && !input.isEmpty())
        {
            input.toInt()
        }
        else
        {
            -1
        }

        return option
    }

    fun databaseMenu() : Int
    {
        var option: Int
        var input: String?

        println("------------------------------------")
        println("Database Options Menu")
        println("1: Export Database to JSON")
        println("2: Import characters from JSON file")
        println("3: Wipe Database (DELETE ALL)")
        println("0: Exit Database Menu")
        print("> ")

        input = readLine()!!

        option = if(input.toIntOrNull() != null && !input.isEmpty())
        {
            input.toInt()
        }
        else
        {
            -1
        }

        return option
    }

    fun listCharacters(charDB: CharacterDBStore)
    {
        println()
        charDB.logAll()
        println()
    }

    fun listCharactersAlphabetically(charDB: CharacterDBStore)
    {
        println()
        charDB.getAllAlphabeticallyByName()
        println()
    }

    fun listCharactersByYear(charDB: CharacterDBStore)
    {
        println()
        charDB.getAllByFirstAppearanceDate()
        println()
    }

    fun loadFromJSON(charDB: CharacterDBStore)
    {
        charDB.readFromJSON()
    }

    fun exportToJSON(charDB: CharacterDBStore)
    {
        charDB.writeToJSON()
    }

    fun wipeDatabase(charDB: CharacterDBStore)
    {
        charDB.wipeDatabase()
    }

    fun wipeJSON(charDB: CharacterDBStore)
    {
        charDB.wipeJSON()
    }

    fun showCharacter(character: CharacterModel)
    {
        if(character != null)
        {
            println("Character Details [ $character ]")
        }
        else
        {
            println("Character not Found...")
        }
    }

    fun showCharacters(chars: List<CharacterModel>)
    {
        if(chars != null && !chars.isEmpty())
        {
            for(c in chars)
            {
                println("Character Details [ $c ]")
            }
        }
        else
        {
            println("No Characters with this Name...")
        }
    }

    fun addCharacterData(character: CharacterModel): Boolean
    {
        try
        {
            println()
            print("Enter a Name: ")
            character.name = readLine()!!

            print("Enter a Description: ")
            character.description = readLine()!!

            print("Enter an Occupation: ")
            character.occupations = readLine()!!

            print("Enter Original Appearance: ")
            character.originalAppearance = readLine()!!

            print("Enter Original Appearance Year: ")
            character.originalAppearanceYear = readLine()!!.toInt()

            if(validateCharacter(character.name, character.description, character.occupations, character.originalAppearance, character.originalAppearanceYear!!))
            {
                return true;
            }
            println("Error with user Inputs")
            return false;
        }
        catch(ex: Exception)
        {
            println("Error with user Inputs")
            return false;
        }
    }

    fun updateCharacterData(character: CharacterModel): Boolean
    {
        val tempName: String?
        val tempDescription: String?
        val tempOccupations: String?
        val tempOriginalAppearance: String?
        val tempOriginalAppearanceYear: Int?

        if(character != null)
        {
            println()
            print("Enter a new Name for [" + character.name + "]: ")
            tempName = readLine()!!

            print("Enter a new Description for [" + character.description + "]: ")
            tempDescription = readLine()!!

            print("Enter a new Occupation for [" + character.occupations + "]: ")
            tempOccupations = readLine()!!

            print("Enter a new Original Appearance for [" + character.originalAppearance + "]: ")
            tempOriginalAppearance = readLine()!!

            print("Enter a new Original Appearance Year for [" + character.originalAppearanceYear + "]: ")
            try
            {
                tempOriginalAppearanceYear = readLine()!!.toInt()
            }
            catch(ex: Exception)
            {
                return false
            }

            if(validateCharacter(tempName, tempDescription, tempOccupations, tempOriginalAppearance, tempOriginalAppearanceYear)) //authenticate valid variables here, could write new method for it.
            {
                //update values and return true
                character.name = tempName
                character.description = tempDescription
                character.occupations = tempOccupations
                character.originalAppearance = tempOriginalAppearance
                character.originalAppearanceYear = tempOriginalAppearanceYear

                return true
            }
        }
        return false
    }

    fun getId() : Long
    {
        var strId: String?
        var searchId: Long

        print("Enter ID to Search/Update: ")
        strId = readLine()!!
        searchId = if(strId.toLongOrNull() != null && !strId.isEmpty())
        {
            strId.toLong()
        }
        else
        {
            -1
        }
        return searchId
    }

    fun getName(): String
    {
        var name: String?

        print("Enter Name to Search/Update: ")
        name = readLine()!!

        return name
    }

    fun validateCharacter(name: String, description: String, occupation: String, originalAppearance: String, appearanceYear: Int): Boolean
    {
        if(checkValidString(name) && checkValidDescription(description) && checkValidString(occupation) && checkValidString(originalAppearance) && checkValidYear(appearanceYear))
        {
            return true
        }
        return false
    }

    fun checkValidString(string: String): Boolean
    {
        if(string.contains("\"") || string.isNullOrEmpty() || string.length > 100)
        {
            return false
        }
        return true
    }

    fun checkValidDescription(string: String): Boolean
    {
        if(string.contains("\"") || string.isNullOrEmpty())
        {
            return false
        }
        return true
    }

    fun checkValidYear(year: Int): Boolean
    {
        //allow between these years inclusive
        if(year > 9999 || year < -9999)
        {
            return false
        }
        return true
    }
}