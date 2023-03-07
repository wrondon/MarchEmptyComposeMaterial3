package com.example.marchemptycomposematerial3


import android.util.Log
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

object MainNetwork {
    var yelpDataAsFlow : Flow<Yelp> = MainNetworkSourceKtor.yelpDataAsFlow

    suspend fun getYelpUsingKtorClnt(what: String, where: String){
        MainNetworkSourceKtor.getYelpUsingKtorClnt(what, where)
    }
}

object MainNetworkSourceKtor {
    private var yelpResponse: Yelp = Yelp()
    var yelpDataAsFlow : Flow<Yelp> = flow {
        repeat(300){
            delay(1000L)
            Log.i("test-01-netw","at netwokr flow emmision (per seccond) yelpResp : $yelpResponse")
            emit(yelpResponse)
        }
    }

    suspend fun getYelpUsingKtorClnt(what: String="fitness", where: String="tampa"){
        val client = HttpClient(CIO){
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val response: HttpResponse = client.get("https://api.yelp.com/v3/businesses/search"){
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.Authorization, "Bearer DV9Ow0NWQbH6_yJW3FAXQvCrH3ehIpmjWKjM8VoZllyijr__96oH_NwhTVW1ZHSFUCUU4W4EEZclqflwokZYtmqnI_ZhKIG-EM0AFFgvwaMv5kb_SwMII3iIf4fmY3Yx")

            }
            url {
                parameters.append("sort_by", "best_match")
                parameters.append("limit", "20")
                parameters.append("location", where)
                parameters.append("term", what)
            }
        }

        if (response.status.isSuccess()){
            yelpResponse = response.body()
            Log.i("test-01-netw","YELp resp using ktor client  (as Yelp object) : (for where:$where & what: $what) \n $yelpResponse")
            println("test-01-netw >>> YELp resp using ktor client  (as Yelp object) : (for where:$where & what: $what) \n $yelpResponse")
        }

        client.close()
    }

}
