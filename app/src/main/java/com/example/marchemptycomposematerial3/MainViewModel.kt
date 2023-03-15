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
import kotlinx.coroutines.runBlocking

class MainViewModel: ViewModel() {
    val uiState : MutableStateFlow<Yelp> = MutableStateFlow(Yelp())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val resp = MainRepo.getYelpUsingKtorClnt("fitness", "orlando")
            resp.collect(){
                uiState.value = it
            }
        }
    }
    fun getYelpUsingKtorClnt(what: String, where: String){
        viewModelScope.launch(Dispatchers.IO) {
            val resp = MainRepo.getYelpUsingKtorClnt(what, where)
            resp.collect(){
                uiState.value = it
            }
        }
    }
}

fun main() {
    runBlocking {
        val vm = MainViewModel()
        launch((Dispatchers.IO)) {
            launch(Dispatchers.IO){
                var resp5 = vm.getYelpUsingKtorClnt("fitness","tampa")
                vm.uiState.collect(){
                    it.businesses.forEach {
                        println(">> ${it.name}")
                    }
                    println("-".repeat(60))
                }
            }

            launch(Dispatchers.IO){
                var resp5 = vm.getYelpUsingKtorClnt("fitness","miami")
                vm.uiState.collect(){
                    it.businesses.forEach {
                        println(">>>> ${it.name}")
                    }
                    println("#".repeat(60))
                }
            }
        }.join()

        println("end")
    }

}