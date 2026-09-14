package com.mofeejegi.alert.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mofeejegi.alert.ui.bannertype.AlertBannerType
import com.mofeejegi.alert.ui.composable.AlertBanner
import com.mofeejegi.alert.ui.composable.rememberAlertManager
import com.mofeejegi.alert.ui.theme.AlertBannerDefaults

@Composable
fun SampleApp() {
    var themeColors by remember { mutableStateOf(false) }

    AlertBanner(
        // The library's colours, or the app theme's, switched live below.
        colors = if (themeColors) {
            AlertBannerDefaults.colors(
                successContainerColor = MaterialTheme.colorScheme.tertiary,
                successContentColor = MaterialTheme.colorScheme.onTertiary,
                errorContainerColor = MaterialTheme.colorScheme.error,
                errorContentColor = MaterialTheme.colorScheme.onError,
                infoContainerColor = MaterialTheme.colorScheme.primary,
                infoContentColor = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            AlertBannerDefaults.colors()
        },
    ) {
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

                Button(
                    onClick = {
                        alertManager.show(
                            message = "This is an info message",
                            type = AlertBannerType.Info,
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(text = "Show Info Alert")
                }

                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Use theme colors")
                    Switch(
                        checked = themeColors,
                        onCheckedChange = { themeColors = it },
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
            }
        }
    }
}
