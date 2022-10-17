package org.assignments.characterdb.models

import java.time.LocalDateTime

//note: occupations can possibly be a list of strings that a user could add to and remove from.
//the rating can be an accumulation of user ratings for the character/character page with a bit of maths, could also be a list
//could have a character image/gallery here too, but we can save that for later.
data class CharacterModel(var id: Long = 0,
                          var name: String = "",
                          var description: String = "",
                          var occupations: String = "",
                          var originalAppearance: String = "",
                          var originalAppearanceYear: Int? = null, //year here because specific dates are bulky and a year is fine
                          var rating: Int? = null, //placeholder for possible user rating, not used currently
                          var dateTimeAdded: LocalDateTime = LocalDateTime.now(), //shouldnt be null, default to the current datetime (updated when added to DB)
                          var lastModified: LocalDateTime? = null) {} //last modified updates when the character is updated