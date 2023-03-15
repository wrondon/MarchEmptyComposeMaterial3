package com.example.marchemptycomposematerial3

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

object MainRepo {

    private val network = MainNetwork

    suspend fun getYelpUsingKtorClnt(what: String="fitness", where: String="tampa") : Flow<Yelp> {
        return flow {
            emit(network.getYelpUsingKtorClnt(what,where))
        }
    }

}

suspend fun main() {
    var resp = MainRepo.getYelpUsingKtorClnt("fitness", "orlando")
    resp.collect() {
        it.businesses.forEach {
            println(">>>> ${it.name}")
        }
        println("-".repeat(60))
    }
    resp = MainRepo.getYelpUsingKtorClnt("fitness", "los angeles")
    resp.collect() {
        it.businesses.forEach {
            println(">>>> ${it.name}")
        }
        println("-".repeat(60))
    }
    resp = MainRepo.getYelpUsingKtorClnt("fitness", "seattle")
    resp.collect() {
        it.businesses.forEach {
            println(">>>> ${it.name}")
        }
        println("-".repeat(60))
    }
}