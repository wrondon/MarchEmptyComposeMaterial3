package com.example.marchemptycomposematerial3


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.marchemptycomposematerial3.ui.theme.MarchEmptyComposeMaterial3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm by viewModels<MainViewModel>()
        setContent {
            MarchEmptyComposeMaterial3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(vm)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel){
    Column{
        var textWhat by remember { mutableStateOf("fitness") }
        var textWhere by remember { mutableStateOf("tampa") }
        val onClickRefresh = {viewModel.getYelpUsingKtorClnt(textWhat,textWhere)}
        val uiState = viewModel.uiState.collectAsState(initial = Yelp())

        Row(horizontalArrangement = Arrangement.SpaceEvenly){
            OutlinedTextField(
                value = textWhat,
                onValueChange = { textWhat = it },
                label = { Text("What?") },
                modifier= Modifier.width(200.dp)
            )
            OutlinedTextField(
                value = textWhere,
                onValueChange = { textWhere = it },
                label = { Text("Where?") },
                modifier= Modifier.width(200.dp)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            Button(onClick = {
                Log.i("test-01-app","clicked on refresh (Ktor-clnt) What: $textWhat  Where: $textWhere")
                onClickRefresh()
            }) {
                Text("KtorClnt")
            }
        }
        Results(uiState.value.businesses)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Results(list : List<Business>) {
    LazyColumn{
        item{
            Text("Results YELP Business")
        }
        items(items=list){
            ListItem(headlineText = { Text(text = "${it.name}")},
                leadingContent = { Image(
                                        painter = rememberAsyncImagePainter(it.image_url),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(50.dp).clip(CircleShape)
                                    )
                                },
                overlineText = {Text(text="${it.phone}")})
        }
    }
}
