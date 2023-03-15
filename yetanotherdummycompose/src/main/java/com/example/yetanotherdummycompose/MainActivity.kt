package com.example.yetanotherdummycompose

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter

import com.example.yetanotherdummycompose.ui.theme.MarchEmptyComposeMaterial3Theme

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val vm by viewModels<MainViewModel>()
        super.onCreate(savedInstanceState)

        Log.i("test13","onCreate Main Activity - STARTED")
        setContent {
            MarchEmptyComposeMaterial3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val l = vm.uiStateListOfPict.collectAsState()
                    ShowUnsplashPictures(l.value, getList = vm::getListOfPictures, searchList = vm::searchPictures)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowUnsplashPictures(listaPic : List<Picture>, getList: ()->Unit, searchList: (String)->Unit){
    
    LazyColumn{
        item{
            Column{
                var text by remember { mutableStateOf("Tampa") }

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Label") }
                )
                Row{
                    Button(onClick = {getList()}) {
                       Text("get List")
                    }
                    Button(onClick = {searchList(text)}) {
                        Text("search text (above)")
                    }
                }
            }
           
        }
        items(items=listaPic){
           /// Text("${it.id} - ${it.description}")
            val descrip = it.description?:it.alt_description
            ListItem(headlineText = {Text(descrip)},
                leadingContent = {
                    Image(
                        painter = rememberAsyncImagePainter(it.urls.small),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                                    },
                    overlineText = {Text("by : ${it.user.name}")})
        }
    }
}

suspend fun main() {
//    val resp1 = DataSourceNetwork.searchPictures("caracas")
//    println(resp1.results.map { "${it.urls.small} \n" })
//    val resp2 = MainRepo.getListOfPictures().collect(){
//        it.forEach {
//            println("${it.id} - ${it.user.name}")
//        }
//    }
//    val resp3 = MainRepo.getRandomPicture().collect(){
//        println(">> ${it.id} - ${it.user.name}")
//    }
      var resp4 = MainRepo.searchPictures("caracas").collect(){
         it.results.forEach {
             println(">>>> (caracas?) ${it.id} - ${it.user.name}")
         }
    }
    resp4 = MainRepo.searchPictures("tampa").collect(){
        it.results.forEach {
            println(">>>>>>>>> (tampa?) ${it.id} - ${it.user.name}")
        }
    }
    resp4 = MainRepo.searchPictures("miami").collect(){
        it.results.forEach {
            println(">>>>>>>>>>>>>> (miami?) ${it.id} - ${it.user.name}")
        }
    }


}

data class UnsplashSearchResult(val total: Int, val total_pages: Int, val results: List<Picture>)

data class Picture(val id: String, val created_at: String, val updated_at: String,val description: String, val alt_description: String, val urls: Urls, val links: Links, val user: User, val tags: List<Tag>)

data class Urls(val raw: String, val full: String, val regular: String, val small: String, val thumb: String, val small_s3: String)

data class Links(val self: String, val html: String, val download: String, val download_location: String)

data class User(val id: String, val username: String, val name: String, val portfolio_url: String, val bio: String, val links: UserLinks)

data class UserLinks(val self: String, val html: String, val photos: String, val likes: String, val portfolio: String,val following: String,val followers: String)

data class Tag(val type: String, val title: String)

object DataSourceNetwork {
    val unsplashService = Retrofit.Builder()
        .baseUrl("https://api.unsplash.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UnsplashServiceApi::class.java)

    suspend fun searchPictures(query: String) = unsplashService.searchPictures(query)

    suspend fun getListOfPictures() = unsplashService.getListOfPictures()

    suspend fun getRandomPicture() = unsplashService.getRandomPicture()

}

interface UnsplashServiceApi {
    @Headers("Authorization:Client-ID YOUR_ACCESS_KEY")
    @GET("search/photos")
    suspend fun searchPictures(@Query("query")query: String): UnsplashSearchResult

    @Headers("Authorization:Client-ID YOUR_ACCESS_KEY")
    @GET("photos")
    suspend fun getListOfPictures(): List<Picture>

    @Headers("Authorization:Client-ID YOUR_ACCESS_KEY")
    @GET("photos/random")
    suspend fun getRandomPicture(): Picture
}

object MainRepo{
    val network = DataSourceNetwork

    suspend fun searchPictures(query: String)   : Flow<UnsplashSearchResult>          {
        return flow {
            val resp = network.searchPictures(query)
            emit(resp)
        }
    }

    suspend fun getListOfPictures()   : Flow<List<Picture>>          {
        return flow {
            val resp = network.getListOfPictures()
            emit(resp)
        }
    }

    suspend fun getRandomPicture()     : Flow<Picture>          {
        return flow {
            val resp = network.getRandomPicture()
            emit(resp)
        }
    }
}

class MainViewModel: ViewModel(){

    val uiStateListOfPict : MutableStateFlow<List<Picture>> =  MutableStateFlow(emptyList())

    val uiStateOnePicture : MutableStateFlow<String> = MutableStateFlow("")

    init{
        viewModelScope.launch(Dispatchers.IO) {
            MainRepo.getListOfPictures().collect(){
                uiStateListOfPict.value = it
            }
        }
    }

    fun getListOfPictures()  {
        viewModelScope.launch(Dispatchers.IO) {
            MainRepo.getListOfPictures().collect(){
                uiStateListOfPict.value = it
            }
        }
    }

    fun searchPictures(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            MainRepo.searchPictures(query).collect(){
                uiStateListOfPict.value = it.results
            }
        }
    }


    fun getRandomPicture() {
        viewModelScope.launch(Dispatchers.IO) {
            MainRepo.getRandomPicture().collect(){
                uiStateOnePicture.value = "${it.id} ${it.user} ${it.created_at}"
            }
        }
    }
}