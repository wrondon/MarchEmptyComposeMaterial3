package com.example.apptemplatebase.data.remote.network

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Singleton


interface NetworkServiceApi {


@GET("search")
suspend fun getYelpSuggestions(@Query("limit") limit: String="20",@Query("sort_by") sortby: String="best_match",@Query("location") location: String="tampa", @Query("term") term: String="fitness" ) : Yelp

}

@Singleton
object NetworkService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.yelp.com/v3/businesses/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val serviceYelp = retrofit.create(NetworkServiceApi::class.java)

    private var currentYelpSuggestions : Yelp = Yelp()

    fun getCurrentYelpSuggestions(): Yelp = currentYelpSuggestions

    suspend fun getYelpSuggestionsFromWeb(term: String, location: String) :Yelp  {
        Log.i("test-02","at network service , asking to search web at api-retrofit  term: $term  and location: $location")
        currentYelpSuggestions = serviceYelp.getYelpSuggestions(term=term, location = location)
        println("test-02 >>> inside network service : calling yelp with loc: $location and term: $term  ; resp: $currentYelpSuggestions")
        Log.i("test-02","at network service , response from retrofit api-retrofit  resp: $currentYelpSuggestions")
        return currentYelpSuggestions
    }

}

@Serializable
data class Yelp(val businesses: List<Business> = emptyList(), val region : Region = Region(), val total : Int = 0)

@Serializable
data class Business(val id: String, val image_url: String, val name: String, val phone: String)

@Serializable
data class Region(val center: Center = Center())

@Serializable
data class Center(val latitude: Double = 27.96 , val longitude: Double = -82.45)


