package org.assignments.characterdb.controllers

import mu.KotlinLogging
import org.assignments.characterdb.models.CharacterJSONStore
import org.assignments.characterdb.models.CharacterModel
import org.assignments.characterdb.models.CharacterMemStore
import org.assignments.characterdb.views.CharacterView

class CharacterController {

    //val characters = CharacterMemStore()
    val characters = CharacterJSONStore()
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
            input = menu()
            when (input) {
                1 -> add()
                2 -> update()
                3 -> list()
                4 -> search()
                5 -> delete()
                6 -> addDummyData()
                0 -> println("Exiting App")
                else -> println("Invalid Option: " + input.toString())
            }
        }
        while (input != 0)

        logger.info{"Exiting Character Database App"}
    }

    fun menu() :Int { return charView.menu() }

    fun add(){
        val aCharacter = CharacterModel()

        if (charView.addCharacterData(aCharacter))
            characters.create(aCharacter)
        else
            logger.info("Character Not Added")
    }

    fun list() {
        charView.listCharacters(characters)
    }

    fun update() {

        charView.listCharacters(characters)
        val searchId = charView.getId()
        val aCharacter = search(searchId)

        if(aCharacter != null) {
            if(charView.updateCharacterData(aCharacter)) {
                characters.update(aCharacter)
                charView.showCharacter(aCharacter)
                logger.info("Character Updated : [ $aCharacter ]")
            }
            else
                logger.info("Character Not Updated")
        }
        else
            println("Character Not Updated...")
    }

    fun delete() {
        charView.listCharacters(characters)
        var searchId = charView.getId()
        val aCharacter = search(searchId)

        if(aCharacter != null) {
            characters.delete(aCharacter)
            println("Character Deleted...")
            charView.listCharacters(characters)
        }
        else
            println("Placemark Not Deleted...")
    }

    fun search() {
        val aCharacter = search(charView.getId())!!
        charView.showCharacter(aCharacter)
    }


    fun search(id: Long) : CharacterModel? {
        val foundChar = characters.getOne(id)
        return foundChar
    }

    fun addDummyData() {
        characters.create(CharacterModel(name = "Superman", description = "Kryptonian who possesses incredible powers.", occupations = "Superhero", originalAppearance = "Action Comics #1", originalAppearanceYear = 1938))
        characters.create(CharacterModel(name = "Arthur Dent", description = "A normal man in a less than normal universe.", occupations = "Sandwich Maker", originalAppearance = "The Hitchhikers Guide to the Galaxy", originalAppearanceYear = 1978))
        characters.create(CharacterModel(name = "Terminator Model 101", description = "Killer robot that looks an awful lot like Arnold Schwarznegger.", occupations = "Killer Robot", originalAppearance = "Terminator", originalAppearanceYear = 1984))
        characters.create(CharacterModel(name = "Clank", description = "Witty robotic sidekick that also functions as a useful backpack.", occupations = "Sidekick", originalAppearance = "Ratchet & Clank", originalAppearanceYear = 2002))
        characters.create(CharacterModel(name = "Misato Katsuragi", description = "Dishes out orders from the HQ.", occupations = "NERV Command", originalAppearance = "Neon Genesis Evangelion", originalAppearanceYear = 1995))
        characters.create(CharacterModel(name = "Son Goku", description = "The question still stands... Can he be beaten?", occupations = "Martial Artist", originalAppearance = "Dragon Ball", originalAppearanceYear = 1984))
        characters.create(CharacterModel(name = "Doctor Octopus", description = "Mad scientist with 4 extra mechanical arms.", occupations = "Mad Scientist", originalAppearance = "The Amazing Spider Man #3", originalAppearanceYear = 1963))
        characters.create(CharacterModel(name = "Solid Snake", description = "A smart and very sneaky elite operative.", occupations = "Special Forces Operative", originalAppearance = "Metal Gear", originalAppearanceYear = 1987))
        characters.create(CharacterModel(name = "Alice", description = "Prone to falling down rabbitholes.", occupations = "Child", originalAppearance = "Alice's Adventures in Wonderland", originalAppearanceYear = 1865))
        characters.create(CharacterModel(name = "Spike Spiegel", description = "A broke bounty hunter who is just trying to make ends meet.", occupations = "Bounty Hunter", originalAppearance = "Cowboy Bebop", originalAppearanceYear = 1998))
        characters.create(CharacterModel(name = "Pintman", description = "45 pints, and I'll be at it again.", occupations = "Pintman", originalAppearance = "The Pub", originalAppearanceYear = 1960))
    }
}