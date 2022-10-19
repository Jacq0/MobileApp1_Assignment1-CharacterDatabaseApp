package org.assignments.characterdb.views

import org.assignments.characterdb.models.CharacterDBStore
import org.assignments.characterdb.models.CharacterJSONStore
import org.assignments.characterdb.models.CharacterMemStore
import org.assignments.characterdb.models.CharacterModel

class CharacterView {

    fun menu(): Int {
        var option: Int
        var input: String?

        println("1: Add a Character")
        println("2: Update a Character")
        println("3: List Characters")
        println("4: Search a Character by ID")
        println("5: Search a Character by Name")
        println("6: Delete a Character")
        println("0: Exit")
        println("> ")

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

    fun listCharacters(characters: CharacterDBStore)
    {
        println()
        characters.logAll()
        println()
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

        return character.name.isNotEmpty() //validate our input, add more secure checks
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
            tempOriginalAppearanceYear = readLine()!!.toInt()


            if(!tempName.isNullOrEmpty()) //authenticate valid variables here, could write new method for it.
            {
                //update values and return true
                character.name = tempName;
                character.description = tempDescription;
                character.occupations = tempOccupations;
                character.originalAppearance = tempOriginalAppearance;
                character.originalAppearanceYear = tempOriginalAppearanceYear;

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
}