package com.example.wikipotter.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wikipotter.data.CharacterRepository
import com.example.wikipotter.model.Characters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel() {
    private val charcRepo: CharacterRepository = CharacterRepository()

    var characters2: MutableLiveData<ArrayList<Characters>> = MutableLiveData<ArrayList<Characters>>()
    private var originList: List<Characters> = listOf()
    private var favsOrOrigin: List<Characters> = listOf()
    private var houseFilters = mutableMapOf<String,Boolean>()
    private var rolFilters = mutableMapOf<String,Boolean>()


    fun init(context: Context){
        //verifico si los map estan vacios
        if (houseFilters.isEmpty() && rolFilters.isEmpty()){
            initFilters()
        }
        CoroutineScope(newSingleThreadContext("characts")).launch {
             kotlin.runCatching {
                 charcRepo.getCharacters(context)
            }.onSuccess {
                Log.d("DEMO_APIS","HarryPotter onSucces")
                originList = it
                 favsOrOrigin = it
                characters2.postValue(it)
             }.onFailure {
                 Log.e("DEMO_APIS","HarryPotter onFailure Error "+it)
                 characters2.postValue(ArrayList<Characters>())
             }
        }
    }

    //aplico filtros
    fun appliedFilter(houses: List<String>, roles: List<String>) {
        //Utilizo la lista favsOrOrigin para poder aplicar filtros en la lista de todos los personajes y tambien en la de favoritos
        val filteredCharacters = favsOrOrigin.filter { character ->
            ((roles.isEmpty() || roles.contains("Staff") && character.hogwartsStaff) ||
                    (roles.isEmpty() || roles.contains("Student") && character.hogwartsStudent)) &&
                    ((houses.isEmpty() || houses.contains(character.house)))
        }
        characters2.postValue(ArrayList(filteredCharacters))
    }

    //vuelvo a poner la lista original
    fun restoreOriginalList() {
        characters2.postValue(originList as ArrayList<Characters>?)
        favsOrOrigin = originList
        houseFilters.clear()
        rolFilters.clear()
    }

    //inicio filtros
    private fun initFilters() {
        houseFilters["Gryffindor"] = false
        houseFilters["Slytherin"] = false
        houseFilters["Hufflepuff"] = false
        houseFilters["Ravenclaw"] = false
        rolFilters["Student"] = false
        rolFilters["Staff"] = false
    }

    //guardo los filtros aplicados
    fun setFilters(houses: Map<String, Boolean>, roles: Map<String,Boolean>){
        houseFilters.clear()
        rolFilters.clear()
        houseFilters.putAll(houses)
        rolFilters.putAll(roles)
    }

    //obtengo los filtros aplicados
    fun getHouseFilters():Map<String,Boolean>{
        return houseFilters
    }

    fun getRolesFilters():Map<String,Boolean>{
        return rolFilters
    }

    //obtengo y mustros los favoritos, elimino filtros aplicados anteriormente.
    fun seeFavorites(email: String){
        CoroutineScope(newSingleThreadContext("characts")).launch {
            kotlin.runCatching {
                charcRepo.getChctsFav(email).await()
            }.onSuccess {querySnapshot ->
                Log.d("DEMO_APIS","HarryPotter Fav onSucces")
                val charactersList = ArrayList<Characters>()
                querySnapshot?.let { snapshot ->
                    for (document in snapshot) {
                        try {
                            val characterData = document.toObject(Characters::class.java)
                            if (characterData != null) {
                                charactersList.add(characterData)
                            } else {
                                Log.e("DEMO_APIS", "Null character data from document: ${document.id}")
                            }
                        } catch (e: Exception) {
                            Log.e("DEMO_APIS", "Error converting document to Characters: ${e.message}")
                        }
                    }
                }
                characters2.postValue(charactersList)
                favsOrOrigin = charactersList
                houseFilters.clear()
                rolFilters.clear()
            }.onFailure {
                Log.e("DEMO_APIS","HarryPotter Fav onFailure Error "+it)
                characters2.postValue(ArrayList<Characters>())
            }
        }
    }
}


