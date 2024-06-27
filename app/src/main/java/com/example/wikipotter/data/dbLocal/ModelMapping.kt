package com.example.wikipotter.data.dbLocal

import androidx.room.PrimaryKey
import com.example.wikipotter.model.Characters
import com.example.wikipotter.model.Wand

fun CharacterLocal.toCharacters(): Characters = Characters(
    id,
    name,
    alternes_name.split(", ") as ArrayList<String> ?: ArrayList<String>(),
    species,
    house,
    dateOfBirth ?: "",
    ancestry ?: "",
    eyeColour ?: "",
    hairColour ?: "",
    (stringToWand(wand)) ?: Wand("","",0.0),
    patronus ?: "",
    hogwartsStudent,
    hogwartsStaff,
    actor,
    image)

fun Characters.toCharacterLocal(): CharacterLocal = CharacterLocal(
    id,
    name,
    alternes_name.joinToString { ", " } ?: "",
    species,
    house,
    dateOfBirth ?: "",
    ancestry ?: "",
    eyeColour ?: "",
    hairColour ?: "",
    "${wand.wood}, ${wand.core}, ${wand.length}" ?: "",
    patronus ?: "",
    hogwartsStudent,
    hogwartsStaff,
    actor,
    image)

private fun stringToWand( str: String): Wand{
    var parts = str.split(", ")
    return Wand(parts[0], parts[1], parts[2].toDouble())
}

fun List<CharacterLocal>.toCharactersList() = map(CharacterLocal::toCharacters)

fun List<Characters>.toCharactersLocalList() = map(Characters::toCharacterLocal)