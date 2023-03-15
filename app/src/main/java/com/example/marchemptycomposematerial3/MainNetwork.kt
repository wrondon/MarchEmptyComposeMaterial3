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

    suspend fun getYelpUsingKtorClnt(what: String, where: String) : Yelp {
        return MainNetworkSourceKtor.getYelpUsingKtorClnt(what, where)
    }
}

object MainNetworkSourceKtor {

    suspend fun getYelpUsingKtorClnt(what: String="fitness", where: String="tampa") : Yelp {

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
                append("Authorization", "Bearer API-KEY")
            }
            url {
                parameters.append("sort_by", "best_match")
                parameters.append("limit", "20")
                parameters.append("location", where)
                parameters.append("term", what)
            }
        }
        client.close()
        return  response.body()
    }
}

suspend fun main() {
    var resp = MainNetworkSourceKtor.getYelpUsingKtorClnt("fitness","tampa")
    resp.businesses.forEach {
        println(">> ${it.name}")
    }
    println("-".repeat(60))
    resp = MainNetworkSourceKtor.getYelpUsingKtorClnt("fitness","miami")
    resp.businesses.forEach {
        println(">> ${it.name}")
    }
    println("-".repeat(60))
    resp = MainNetworkSourceKtor.getYelpUsingKtorClnt("fitness","new york")
    resp.businesses.forEach {
        println(">> ${it.name}")
    }
}
