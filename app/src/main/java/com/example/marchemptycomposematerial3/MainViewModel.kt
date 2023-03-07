package com.example.marchemptycomposematerial3

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    val uiState : MutableStateFlow<Yelp> = MutableStateFlow(Yelp())

    init {
        viewModelScope.launch {
            MainRepo.getYelpUsingKtorClnt("fitness", "tampa")
            MainRepo.yelpDataAsFlow.collect(){
                uiState.value = it
                Log.i("test-01-vm","at View model collecting from repo")
            }
        }
    }

    fun getYelpUsingKtorClnt(what: String="fitness", where: String="tampa"){
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("test-01-vm","at View model calling getYelpUsingKtorClnt down in repo")
            MainRepo.getYelpUsingKtorClnt(what, where)
        }
    }
}