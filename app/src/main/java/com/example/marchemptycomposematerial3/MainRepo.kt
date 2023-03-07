package com.example.marchemptycomposematerial3

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object MainRepo {

    private val network = MainNetwork

    var yelpDataAsFlow : Flow<Yelp> = network.yelpDataAsFlow

    suspend fun getYelpUsingKtorClnt(what: String="fitness", where: String="tampa"){
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("test-01-rep","at Repo, calling get Yelp using ktor with network ...")
            network.getYelpUsingKtorClnt(what, where)
        }
    }

}