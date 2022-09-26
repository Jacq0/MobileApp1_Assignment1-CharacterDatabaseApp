package org.assignments.characterdb.models

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class CharacterMemStore: CharacterStore {

    var characters = ArrayList<CharacterModel>()

    override fun getAll(): List<CharacterModel>
    {
        return characters
    }

    override fun getOne(id: Long): CharacterModel? {
        var foundCharacter: CharacterModel? = characters.find{c -> c.id == id}
        return foundCharacter
    }

    override fun create(character: CharacterModel)
    {
        character.id = getId()
        characters.add(character)
        logAll()
    }

    override fun update(character: CharacterModel)
    {
        var foundCharacter = getOne(character.id)
        if(foundCharacter != null)
        {
            foundCharacter.name = character.name
            foundCharacter.description = character.description
            foundCharacter.occupations = character.occupations
            foundCharacter.originalAppearance = character.originalAppearance
            foundCharacter.originalAppearanceYear = character.originalAppearanceYear
        }
    }

    internal fun logAll(){
        characters.forEach { logger.info("${it}") }
    }

    override fun delete(character: CharacterModel) {
        characters.remove(character)
    }
}