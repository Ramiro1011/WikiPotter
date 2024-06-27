package com.example.wikipotter.data

import android.content.Context
import android.util.Log
import com.example.wikipotter.data.dbLocal.AppDataBase
import com.example.wikipotter.data.dbLocal.CharacterLocal
import com.example.wikipotter.data.dbLocal.toCharacters
import com.example.wikipotter.data.dbLocal.toCharactersList
import com.example.wikipotter.data.dbLocal.toCharactersLocalList
import com.example.wikipotter.model.Characters
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CharacterDataSource {
    companion object{
        private val API_BASE_URL = "https://hp-api.onrender.com/api/"
        private val api : CharacterApi
        private val db = FirebaseFirestore.getInstance()

        init{
            api = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(CharacterApi::class.java)
        }

        suspend fun getCharactersHouse(house:String): ArrayList<Characters> {
            Log.d("DEMO_APIS", "HarryPotter Characters Datasource Get House")

            var result = api.getCharactersHouse(house).execute()

            return if (result.isSuccessful) {
                Log.d("DEMO_APIS", "HarryPotter Characters Datasource result Exitoso")
                result = setImage(result)
                result.body() ?: ArrayList<Characters>()
            } else {
                Log.e("DEMO_APIS", "HarryPotter Characters Datasource result Error" + result.message())
                ArrayList<Characters>()
            }
        }

        suspend fun getCharacters(context: Context):ArrayList<Characters>{
            Log.d("DEMO_APIS", "HarryPotter Characters Datasource Get")

            //Recupero los personajes de la base de datos local (si existen)
            var db =AppDataBase.getInstance(context)
            var caractersLocal = db.charactersDAO().getAll()
            if (caractersLocal.size>0){
                Log.d("DEMO_APIS", "devuelvo lista Local")
                return caractersLocal.toCharactersList() as ArrayList<Characters>
            }

            //Recupero todos los personajes de la api
            var result = api.getCharacters().execute()

            return if (result.isSuccessful) {
                Log.d("DEMO_APIS", "HarryPotter Characters Datasource result Exitoso")
                result = setImage(result)

                var uList = result.body() ?: ArrayList<Characters>()
                if (uList.isNotEmpty()){
                    db.charactersDAO().save(*uList.toCharactersLocalList().toTypedArray())
                }
                uList
            } else {
                Log.e("DEMO_APIS", "HarryPotter Characters Datasource result Error" + result.message())
                ArrayList<Characters>()
            }

        }

        suspend fun getCharacterId(id:String,context: Context): Characters? {
            Log.d("DEMO_APIS", "HarryPotter CharacterId Datasource Get")

            //recupero un personaje de la bd local
            var db =AppDataBase.getInstance(context)
            var caracterLocal = db.charactersDAO().getById(id)
            if (caracterLocal.id.length>0){
                Log.d("DEMO_APIS", "devuelvo lista Local")
                return caracterLocal.toCharacters()
            }

            //recupero un personaje de la api
            var result = api.getCharacterId(id).execute()

            return if (result.isSuccessful) {
                Log.d("DEMO_APIS", "HarryPotter CharacterId Datasource result Exitoso")
                result = setImage(result)
                val characters = result.body() ?: return null
                return characters.singleOrNull()
            } else {
                Log.e("DEMO_APIS", "HarryPotter CharacterId Datasource result Error" + result.message())
                return null
            }
        }

        suspend fun setCharcFav(email: String,character: Characters){
            try {
                Log.d("DEMO_APIS", "HarryPotter SetEmailId Datasource")
                db.collection("favUser").document(email).collection("favoritos").document(character.id).set(character)
            } catch (e:Exception){
                e.printStackTrace()
            }

        }

        suspend fun getChrtsFav(email: String): Task<QuerySnapshot> {
            return try{
                db.collection("favUser").document(email).collection("favoritos").get()
            } catch (e:Exception){
                e.printStackTrace()
                Tasks.forException<QuerySnapshot>(e)
            }
        }

        suspend fun getChrtFav(email:String, id: String): Boolean{
            return try {
                val chrt = db.collection("favUser").document(email).collection("favoritos").document(id).get().await()
                chrt.exists()
            } catch (e:Exception){
                e.printStackTrace()
                false
            }
        }

        suspend fun removeChrctFav(email: String, id: String) {
            try {
                db.collection("favUser").document(email).collection("favoritos").document(id)
                    .delete()
            } catch (e:Exception){
                e.printStackTrace()
            }
        }

        private fun setImage(lista: Response<ArrayList<Characters>>): Response<ArrayList<Characters>> {
            var listaa = lista.body()
            if (listaa != null) {
                for (e in listaa){
                    if (e.image =="")
                        e.image = "https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y"
                }
            }
            return lista
        }

    }
}