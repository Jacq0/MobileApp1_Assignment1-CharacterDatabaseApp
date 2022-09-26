package org.assignments.characterdb.views

import org.assignments.characterdb.models.CharacterJSONStore
import org.assignments.characterdb.models.CharacterMemStore
import org.assignments.characterdb.models.CharacterModel

class CharacterView {

    fun menu(): Int {
        var option: Int
        var input: String?

        println("1: Add a Character")
        println("2: Update a Character")
        println("3: List a Character")
        println("4: Search a Character")
        println("5: Delete a Character")
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

    fun listCharacters(characters: CharacterJSONStore)
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

        return character.name.isNotEmpty() //validate our input
    }

    fun updateCharacterData(character: CharacterModel): Boolean
    {
        val tempName: String?
        val tempDescrption: String?
        val tempOccupations: String?
        val tempOriginalAppearance: String?
        val tempOriginalAppearanceYear: Int?

        if(character != null)
        {
            println()
            print("Enter a new Name for [" + character.name + "]: ")
            tempName = readLine()!!

            print("Enter a new Description for [" + character.description + "]: ")
            tempDescrption = readLine()!!

            print("Enter a new Occupation for [" + character.occupations + "]: ")
            tempOccupations = readLine()!!

            print("Enter a new Original Appearance for [" + character.originalAppearance + "]: ")
            tempOriginalAppearance = readLine()!!

            print("Enter a new Original Appearance Year for [" + character.originalAppearanceYear + "]: ")
            tempOriginalAppearanceYear = readLine()!!.toInt()


            if(!tempName.isNullOrEmpty())
            {
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
}