package com.example.apptemplatebase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apptemplatebase.ui.dataitemtype.DataItemTypeViewModel
import com.example.apptemplatebase.ui.theme.MainFebComposeMaterial3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainFebComposeMaterial3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewMainScreen()
                }
            }
        }
    }
}

@Composable
fun NewMainScreen(modifier: Modifier = Modifier, viewModel: DataItemTypeViewModel = hiltViewModel()) {
    val sf = viewModel.uiState2API.collectAsState()
    val list = sf.value.businesses
    Column{
        Text("What are we doing?", Modifier.clickable(){
            Log.i("test-02","clicked in text, asking to search web at view model")
            val locat = listOf("miami","tampa","new york","san francisco").shuffled().first()
            viewModel.getYelpSuggestionsFromWeb("fitness",locat)
        })
        LazyColumn{
            items(items=list){
                Text("${it.name} - ${it.phone}")
            }
        }
    }
}

