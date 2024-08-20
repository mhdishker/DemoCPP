package com.proximie.democpp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.proximie.democpp.ui.theme.DemoCTheme
import com.proximie.externallib.NativeLib

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = NativeLib().stringFromAAR(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    external fun stringSoLib(): String?
    external fun stringFromCPP(): String?
    companion object {
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("binary-lib")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoCTheme {
        Greeting("Android")
    }
}