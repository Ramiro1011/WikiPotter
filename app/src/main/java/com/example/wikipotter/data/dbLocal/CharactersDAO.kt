package com.example.wikipotter.data.dbLocal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wikipotter.model.Characters

@Dao
interface CharactersDAO {
    @Query("Select * from Characters")
    fun getAll(): List<CharacterLocal>

    @Query("Select * from Characters where id = :id")
    fun getById(id: String): CharacterLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg character: CharacterLocal)

    @Delete
    fun delete(character: CharacterLocal)

}