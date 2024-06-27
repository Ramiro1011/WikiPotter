package com.example.wikipotter.data

import android.content.Context
import android.util.Log
import com.example.wikipotter.model.Characters
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class CharacterRepository {

    //Los personajes pertenecientes a una determinada casa
    suspend fun getCharactersHouse(house:String): ArrayList<Characters>{
        return CharacterDataSource.Companion.getCharactersHouse(house)
    }
    //Todos los personajes
    suspend fun getCharacters(context: Context): ArrayList<Characters>{
        return CharacterDataSource.Companion.getCharacters(context)
    }

    //Un personaje por id
    suspend fun getCharacterId(id:String,context: Context): Characters? {
        return CharacterDataSource.Companion.getCharacterId(id, context)
    }
    //Setea el favorito
    suspend fun setChrctFav(email:String,character: Characters){
        CharacterDataSource.Companion.setCharcFav(email,character)
    }
    //Todos los favoritos
    suspend fun getChctsFav(email:String): Task<QuerySnapshot> {
        return CharacterDataSource.Companion.getChrtsFav(email)
    }
    //Un favorito en particular
    suspend fun getChctFav(email:String,id:String): Boolean {
        var c =CharacterDataSource.Companion.getChrtFav(email,id)
        Log.d("DEMO_APIS","Get Character Repository:"+c.toString())
        return c
    }
    //Elimina el favorito
    suspend fun removeChrctFav(email: String, id: String) {
        CharacterDataSource.Companion.removeChrctFav(email,id)

    }
}