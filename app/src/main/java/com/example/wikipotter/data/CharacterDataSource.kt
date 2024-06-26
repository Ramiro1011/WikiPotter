package com.example.wikipotter.data

import android.util.Log
import com.example.wikipotter.model.Characters
import com.google.android.gms.tasks.Task
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

        suspend fun getCharacters():ArrayList<Characters>{
            Log.d("DEMO_APIS", "HarryPotter Characters Datasource Get")
            var result = api.getCharacters().execute()

            return if (result.isSuccessful) {
                Log.d("DEMO_APIS", "HarryPotter Characters Datasource result Exitoso")
                result = setImage(result)
                result.body() ?: ArrayList<Characters>()
            } else {
                Log.e("DEMO_APIS", "HarryPotter Characters Datasource result Error" + result.message())
                ArrayList<Characters>()
            }

        }

        suspend fun getCharacterId(id:String): Characters? {
            Log.d("DEMO_APIS", "HarryPotter CharacterId Datasource Get")

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
            Log.d("DEMO_APIS", "HarryPotter SetEmailId Datasource")
            db.collection("favUser").document(email).collection("favoritos").document(character.id).set(character)
        }

        suspend fun getChrtsFav(email: String): Task<QuerySnapshot> {
            return db.collection("favUser").document(email).collection("favoritos").get()
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
            db.collection("favUser").document(email).collection("favoritos").document(id).delete()
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