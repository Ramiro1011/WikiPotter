package com.example.wikipotter.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wikipotter.data.CharacterRepository
import com.example.wikipotter.model.Characters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class CharacterViewModel: ViewModel() {
    private val charcRepo: CharacterRepository = CharacterRepository()
    var characters2: MutableLiveData<Characters> = MutableLiveData<Characters>()

    fun init(id:String){
        CoroutineScope(newSingleThreadContext("character")).launch {
            kotlin.runCatching {
                charcRepo.getCharacterId(id)
            }.onSuccess {
                Log.d("DEMO_APIS","HarryPotter onSucces")
                characters2.postValue(it ?: Characters())
            }.onFailure {
                Log.e("DEMO_APIS","HarryPotter onFailure Error "+it)
                val charct = Characters()
                charct.name = "Error"
                characters2.postValue(charct)
            }
        }

    }

}