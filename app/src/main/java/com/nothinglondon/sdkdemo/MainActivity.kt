
package com.nothinglondon.sdkdemo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nothinglondon.sdkdemo.ui.theme.NothingAndroidSDKDemoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferences = getSharedPreferences(
            "glyph_settings",
            Context.MODE_PRIVATE
        )

        setContent {
            NothingAndroidSDKDemoTheme {

                var staticText by remember {
                    mutableStateOf(
                        preferences.getString("static_text", "CUP") ?: "CUP"
                    )
                }

                var scrollText by remember {
                    mutableStateOf(
                        preferences.getString("scroll_text", "CUPSIZE") ?: "CUPSIZE"
                    )
                }

                var saved by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {

                        Text(
                            text = "CUPSIZE GLYPH",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Text(
                            text = "Настрой свою Glyph Matrix",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        HorizontalDivider()

                        Text(
                            text = "Основная надпись",
                            style = MaterialTheme.typography.titleMedium
                        )

                        OutlinedTextField(
                            value = staticText,
                            onValueChange = {
                                staticText = it
                                saved = false
                            },
                            label = { Text("Первые 3 символа") },
                            supportingText = {
                                Text("На матрице отображаются первые 3 символа")
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Текст при удержании",
                            style = MaterialTheme.typography.titleMedium
                        )

                        OutlinedTextField(
                            value = scrollText,
                            onValueChange = {
                                scrollText = it
                                saved = false
                            },
                            label = { Text("Текст для прокрутки") },
                            supportingText = {
                                Text("Прокручивается при удержании кнопки Glyph")
                            },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                preferences.edit()
                                    .putString("static_text", staticText)
                                    .putString("scroll_text", scrollText)
                                    .apply()

                                saved = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Сохранить настройки")
                        }

                        if (saved) {
                            Text(
                                text = "Настройки сохранены!",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        HorizontalDivider()

                        Text(
                            text = "Предпросмотр",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "На матрице: ${
                                staticText.uppercase().take(3)
                            }"
                        )

                        Text(
                            text = "При удержании: ${
                                scrollText.uppercase()
                            }"
                        )

                        Text(
                            text = "После сохранения переключи Glyph Toy, чтобы обновить надпись.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
