package org.assignments.characterdb.models

interface CharacterStore
{
    fun getAll(): List<CharacterModel>
    fun getOne(id: Long): CharacterModel?
    fun create(character: CharacterModel)
    fun update(character: CharacterModel)
}