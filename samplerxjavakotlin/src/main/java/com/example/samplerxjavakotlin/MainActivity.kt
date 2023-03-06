package com.example.samplerxjavakotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.samplerxjavakotlin.ui.theme.MarchEmptyComposeMaterial3Theme
import io.reactivex.rxjava3.core.Observable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Observable.just("Rx", "Java", "Kotlin")
            .map({ //input -> throw RuntimeException()
                    input -> "changed happened! $input" } )
            .subscribe(
                { value -> Log.i("test-01-rx","Received: $value") },
                { error -> Log.i("test-01-rx","Error: $error") },
                { Log.i("test-01-rx","and now all is Completed!") }
            ).dispose()
        setContent {
            MarchEmptyComposeMaterial3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android-RxJavaKotlin")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Olà $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MarchEmptyComposeMaterial3Theme {
        Greeting("Android")
    }
}