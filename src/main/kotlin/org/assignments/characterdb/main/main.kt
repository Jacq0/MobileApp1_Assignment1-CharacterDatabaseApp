package org.assignments.characterdb.main

import mu.KotlinLogging
import org.assignments.characterdb.models.CharacterMemStore
import org.assignments.characterdb.models.CharacterModel
import org.assignments.characterdb.views.CharacterView

private val logger = KotlinLogging.logger {}

var characters = CharacterMemStore()
val characterView = CharacterView()

fun main() {
    logger.info { "Launching Character Database App" }
    println("Character Database App v1.0")

    var input: Int

    do {
        input = characterView.menu()
        when (input) {
            1 -> addCharacter()
            2 -> updateCharacter()
            3 -> characterView.listCharacters(characters)
            4 -> searchCharacter()
            0 -> println("Exiting App")
        }
    }
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
    val aCharacter = CharacterModel()

    if(characterView.addCharacterData((aCharacter)))
    {
        characters.create(aCharacter)
    }
    else
    {
        logger.info("Character Not Added")
    }
}
fun searchCharacter()
{

}
fun allMandatoryFieldsFilled(character: CharacterModel): Boolean
{
    return true
}

fun updateCharacter()
{

}

fun getCharacters()
{

}

fun charSelectionList(): Long
{

}

fun deleteCharacter()
{

}