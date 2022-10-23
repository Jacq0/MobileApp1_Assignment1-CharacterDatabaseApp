package org.assignments.characterdb.models

interface CharacterStore
{
    fun getAll(): List<CharacterModel>
    fun getAllAlphabeticallyByName()
    fun getAllByFirstAppearanceDate()
    fun getOne(id: Long): CharacterModel?
    fun getByName(name: String): List<CharacterModel>?
    fun create(character: CharacterModel)
    fun update(character: CharacterModel)
    fun delete(character: CharacterModel)
    fun writeToJSON()
    fun readFromJSON()
    fun wipeDatabase()
    fun wipeJSON()
}