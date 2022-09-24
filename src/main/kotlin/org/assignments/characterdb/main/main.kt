package org.assignments.characterdb.main

import org.assignments.characterdb.models.CharacterModel

var character = CharacterModel() //base character model with no data
var characters =  ArrayList<CharacterModel>() //array list of all characters

fun main()
{
    println("Welcome to the Character Database App v1.0")

    var input: Int = -1

    do
    {
        input = menu()
        when(input)
        {
            1 -> addCharacter()
            2 -> updateCharacter()
            3 -> getCharacterList()
            4 -> deleteCharacter()
            0 -> println("Bye Bye o7")
            else -> println("Invalid Option Selected")
        }
    }
    while(input != 0)
}

fun menu() : Int
{
    //basic CLI menu with DB then we can expand it with GUI
    var option: Int
    var input: String? = null

    println("1 - Add a Character")
    println("2 - Update existing Character")
    println("3 - View Characters")
    println("4 - Delete a Characer")
    println("0 - Exit Application")

    input = readLine()!!
    option = input.toInt()

    return option
}

fun addCharacter()
{
    println("------------------")
    println("Add Character")
    println("------------------")

    println("Enter Character Name: ")
    character.name = readLine()!!

    println("Enter Character Description: ")
    character.description = readLine()!!

    println("Enter Character Occupation: ")
    character.occupations = readLine()!!

    println("Enter Characters First Appearance: ")
    character.originalAppearance = readLine()!!

    println("Enter Characters First Appearance Year: ")
    character.originalAppearanceYear = readLine()!!.toInt()

    //check if we can add the character with this data
    if(allMandatoryFieldsFilled(character))
    {
        characters.add(character)
    }
    else
    {
        //couldn't add character, throw error to user.
    }
}

fun allMandatoryFieldsFilled(character: CharacterModel): Boolean
{
    return true
}

fun updateCharacter()
{

}

fun getCharacterList()
{
    for (c in characters)
    {
        println(c)
    }
}

fun deleteCharacter()
{

}