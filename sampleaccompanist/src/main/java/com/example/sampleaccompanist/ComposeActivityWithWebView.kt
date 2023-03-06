package com.example.sampleaccompanist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sampleaccompanist.ui.theme.MarchEmptyComposeMaterial3Theme
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

class ComposeActivityWithWebView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarchEmptyComposeMaterial3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2("Android-Compose-Accompanist-WebView")
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Column{
        Text(
            text = "Salve $name!",
            modifier = modifier
        )
        Divider()
        val state = rememberWebViewState("https://news.ycombinator.com/")

        WebView(
            state
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MarchEmptyComposeMaterial3Theme {
        Greeting2("Android")
    }
}