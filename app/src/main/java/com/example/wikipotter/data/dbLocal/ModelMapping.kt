package com.example.wikipotter.data.dbLocal

import androidx.room.PrimaryKey
import com.example.wikipotter.model.Characters
import com.example.wikipotter.model.Wand

fun CharacterLocal.toCharacters(): Characters = Characters(
    id,
    name,
    stringToArray(alternes_name),
    species,
    house,
    dateOfBirth ?: "",
    ancestry ?: "",
    eyeColour ?: "",
    hairColour ?: "",
    (stringToWand(wand)),
    patronus ?: "",
    hogwartsStudent,
    hogwartsStaff,
    actor,
    image)

fun Characters.toCharacterLocal(): CharacterLocal = CharacterLocal(
    id,
    name,
    arrayToString(alternes_name),
    species,
    house,
    dateOfBirth ?: "",
    ancestry ?: "",
    eyeColour ?: "",
    hairColour ?: "",
    wandToString(wand),
    patronus ?: "",
    hogwartsStudent,
    hogwartsStaff,
    actor,
    image)

private fun stringToWand( str: String): Wand{
    if (str == ""){
        return Wand("","",0.0)
    }
    var parts = str.split(", ")
    return Wand(parts[0], parts[1], parts[2].toDouble())
}

private fun stringToArray(str: String): ArrayList<String>{
    if (str == ""){
        return ArrayList<String>()
    }
    return str.split( ", " ).toCollection(ArrayList<String>())
}

private fun arrayToString(a: ArrayList<String>): String{
    if (a.size==0){
        return ""
    }
    return a.joinToString { ", " }
}

private fun wandToString(wand: Wand): String{
    var str = ArrayList<String>()
    if (wand.wood!= "") {
        str.add(wand.wood)
    }
    if (wand.core != ""){
        str.add(wand.core)
    }
    return arrayToString(str)
}

fun List<CharacterLocal>.toCharactersList() = map(CharacterLocal::toCharacters)

fun List<Characters>.toCharactersLocalList() = map(Characters::toCharacterLocal)