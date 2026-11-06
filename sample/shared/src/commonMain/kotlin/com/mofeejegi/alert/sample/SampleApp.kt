package com.mofeejegi.alert.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mofeejegi.alert.ui.bannertype.AlertBannerType
import com.mofeejegi.alert.ui.composable.AlertBanner
import com.mofeejegi.alert.ui.composable.rememberAlertManager

@Composable
fun SampleApp() {
    AlertBanner {
        val alertManager = rememberAlertManager()

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
                        alertManager.show(
                            message = "This is a success message",
                            type = AlertBannerType.Success,
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(text = "Show Success Alert")
                }

                Button(
                    onClick = {
                        alertManager.show(
                            message = "This is an error message",
                            type = AlertBannerType.Error,
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(text = "Show Error Alert")
                }

                val customType = AlertBannerType.Custom(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
                Button(
                    onClick = {
                        alertManager.show(
                            message = "This is a custom message",
                            type = customType,
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(text = "Show Custom Alert")
                }
            }
        }
    }
}
