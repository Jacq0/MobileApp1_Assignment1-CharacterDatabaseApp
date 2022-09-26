package org.assignments.characterdb.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging

import org.assignments.characterdb.helpers.*
import java.util.*

private val logger = KotlinLogging.logger {}

val JSON_FILE = "characters.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<CharacterModel>>() {}.type

fun generateRandomId(): Long
{
    return Random().nextLong()
}

class CharacterJSONStore : CharacterStore
{
    var characters = mutableListOf<CharacterModel>()

    init {
        if (exists(JSON_FILE)) {
            deserialize()
        }
    }

    override fun getAll(): MutableList<CharacterModel> {
        return characters
    }

    override fun getOne(id: Long) : CharacterModel? {
        var foundChar: CharacterModel? = characters.find { c -> c.id == id }
        return foundChar
    }

    override fun create(character: CharacterModel) {
        character.id = generateRandomId()
        characters.add(character)
        serialize()
    }

    override fun update(character: CharacterModel) {
        var foundChar = getOne(character.id!!)
        if (foundChar != null) {
            foundChar.name = character.name
            foundChar.description = character.description
            foundChar.occupations = character.occupations
            foundChar.originalAppearance = character.originalAppearance
            foundChar.originalAppearanceYear = character.originalAppearanceYear
        }
        serialize()
    }

    override fun delete(character: CharacterModel) {
        characters.remove(character)
        serialize()
    }

    internal fun logAll() {
        characters.forEach { logger.info("${it}") }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(characters, listType)
        write(JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(JSON_FILE)
        characters = Gson().fromJson(jsonString, listType)
    }
}