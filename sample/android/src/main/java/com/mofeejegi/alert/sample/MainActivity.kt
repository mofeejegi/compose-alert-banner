package com.mofeejegi.alert.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mofeejegi.alert.sample.ui.theme.SampleTheme
import com.mofeejegi.alert.ui.composable.AlertBanner
import com.mofeejegi.alert.ui.composable.rememberAlertManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleTheme {
                AlertBanner {
                    val alertManager = rememberAlertManager()

                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Greeting(
                                name = "Android",
                                modifier = Modifier.padding(innerPadding)
                            )

                            Button(
                                onClick = {
                                    alertManager.show(
                                        message = "This is a success message",
                                        type = com.mofeejegi.alert.ui.bannertype.AlertBannerType.Success,
                                    )
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Show Success Alert")
                            }
                        }
                    }
                }
            }
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
    SampleTheme {
        Greeting("Android")
    }
}