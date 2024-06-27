package com.example.wikipotter.data.dbLocal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CharacterLocal::class],
    version = 1,
    exportSchema = false
)

abstract class AppDataBase: RoomDatabase() {
    abstract fun charactersDAO() : CharactersDAO

    companion object{
        @Volatile // Se puede acceeder desde multiples hilos de ejecucion
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase = instance ?: synchronized(this){
            instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) : AppDataBase = Room.databaseBuilder(context, AppDataBase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}