package org.assignments.characterdb.models

//note: occupations can be a list of strings that a user could add to and remove from.
//the rating can be an accumulation of user ratings for the character/character page with a bit of maths, could also be a list
//could have a character image/gallery here too, but we can save that for later.
data class CharacterModel(var id: Long = 0,
                          var name: String = "",
                          var description: String = "",
                          var occupations: String = "",
                          var originalAppearance: String = "",
                          var originalAppearanceYear: Int? = null,
                          var rating: Int? = null) {}