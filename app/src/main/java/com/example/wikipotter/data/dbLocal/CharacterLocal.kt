package com.example.wikipotter.data.dbLocal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.wikipotter.model.Wand


@Entity(tableName = "Characters")
data class CharacterLocal (
    @PrimaryKey var id: String,
    var name: String,
    var alternes_name: String,
    var species: String,
    var house: String,
    var dateOfBirth: String,
    var ancestry: String,
    var eyeColour: String,
    var hairColour: String,
    var wand: String,
    var patronus: String,
    var hogwartsStudent: Boolean,
    var hogwartsStaff: Boolean,
    var actor: String,
    var image: String)