package com.example.wikipotter.model


class Characters(
    val id: String,
    var name: String,
    var alternes_name: ArrayList<String>,
    var species: String,
    var house: String,
    var dateOfBirth: String,
    var yearOfBirth: Int,
    var wizart: Boolean,
    var ancestry: String,
    val eyeColour: String,
    val hairColour: String,
    val wand: Wand,
    val patronus: String,
    val hogwartsStudent: Boolean,
    val hogwartsStaff: Boolean,
    val actor: String,
    val alternate_actors: ArrayList<String>,
    val alive: Boolean,
    var image: String
){
    constructor(): this("","",ArrayList<String>(),"","","",0,false,"","","",
        Wand("","",0.0),"",false,false,"",
        ArrayList<String>(),false,"")
}