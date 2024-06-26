package com.example.wikipotter.data

import retrofit2.Call
import com.example.wikipotter.model.Characters
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApi {
    @GET("characters/house/{house}")
    fun getCharactersHouse(
        @Path("house") house:String
    ): Call<ArrayList<Characters>>

    @GET("characters")
    fun getCharacters(): Call<ArrayList<Characters>>

    @GET("character/{id}")
    fun getCharacterId(
        @Path("id") id:String
    ): Call<ArrayList<Characters>>
}