package com.example.wikipotter.model


class Characters(
    val id: String,
    var name: String,
    var alternes_name: ArrayList<String>,
    var species: String,
    var house: String,
    var dateOfBirth: String,
    var ancestry: String,
    val eyeColour: String,
    val hairColour: String,
    val wand: Wand,
    val patronus: String,
    val hogwartsStudent: Boolean,
    val hogwartsStaff: Boolean,
    val actor: String,
    var image: String
){
    constructor(): this("","",ArrayList<String>(),"","","","","","",Wand("","",0.0),"",
        false,false,"","")
}