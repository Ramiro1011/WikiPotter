package com.example.wikipotter.data

import android.util.Log
import com.example.wikipotter.model.Characters
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class CharacterRepository {


    suspend fun getCharactersHouse(house:String): ArrayList<Characters>{
        return CharacterDataSource.Companion.getCharactersHouse(house)
    }
    suspend fun getCharacters(): ArrayList<Characters>{
        return CharacterDataSource.Companion.getCharacters()
    }
    suspend fun getCharacterId(id:String): Characters? {
        return CharacterDataSource.Companion.getCharacterId(id)
    }
    suspend fun setChrctFav(email:String,character: Characters){
        CharacterDataSource.Companion.setCharcFav(email,character)
    }
    suspend fun getChctsFav(email:String): Task<QuerySnapshot> {
        return CharacterDataSource.Companion.getChrtsFav(email)
    }
    suspend fun getChctFav(email:String,id:String): Boolean {
        var c =CharacterDataSource.Companion.getChrtFav(email,id)
        Log.d("DEMO_APIS","Get Character Repository:"+c.toString())
        return c
    }
    suspend fun removeChrctFav(email: String, id: String) {
        CharacterDataSource.Companion.removeChrctFav(email,id)

    }
}