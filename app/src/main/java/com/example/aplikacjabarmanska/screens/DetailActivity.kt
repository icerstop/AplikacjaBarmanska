package com.example.aplikacjabarmanska.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.aplikacjabarmanska.ui.theme.AplikacjaBarmanskaTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.lifecycle.ViewModelProvider
import androidx.activity.compose.BackHandler
import com.example.aplikacjabarmanska.data.ThemeManager
import com.example.aplikacjabarmanska.ui.theme.LightAppBarColor
import com.example.aplikacjabarmanska.ui.theme.DarkAppBarColor

class DetailActivity : ComponentActivity() {
    private lateinit var timerViewModel: TimerViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isTablet = resources.configuration.screenWidthDp >= 600
        if (isTablet) {
            // W trybie tabletowym nie pokazujemy oddzielnego activity
            // po prostu kończymy tę aktywność i wracamy do MainActivity
            finish()
            return
        }

        val cocktailId = intent.getIntExtra("cocktailId", -1)

        timerViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[TimerViewModel::class.java]

        timerViewModel.setCocktailId(cocktailId)

        setContent {
            AplikacjaBarmanskaTheme {
                BackHandler {
                    finish()
                }

                val context = LocalContext.current
                var cocktail by remember { mutableStateOf<com.example.aplikacjabarmanska.model.Cocktail?>(null) }

                // ThemeManager do kontroli trybu ciemnego
                val themeManager = ThemeManager.getInstance()
                val isDarkMode by themeManager.isDarkTheme.collectAsState()

                LaunchedEffect(cocktailId) {
                    val db = com.example.aplikacjabarmanska.model.CocktailDatabase.getDatabase(context)
                    cocktail = db.cocktailDao().getAll().find { it.id == cocktailId }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    cocktail?.name ?: "Szczegóły koktajlu"
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronLeft,
                                        contentDescription = "Wróć",
                                        tint = if (isDarkMode) Color.White else Color.Black
                                    )
                                }
                            },
                            actions = {
                                // Przełącznik trybu ciemnego
                                IconButton(onClick = { themeManager.toggleTheme() }) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = if (isDarkMode) "Przełącz na tryb jasny" else "Przełącz na tryb ciemny",
                                        tint = if (isDarkMode) Color.White else Color.Black
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = if (isDarkMode) DarkAppBarColor else LightAppBarColor,
                                titleContentColor = if (isDarkMode) Color.White else Color.Black
                            )
                        )
                    }
                ) { innerPadding ->
                    CocktailDetailScreenContent(
                        cocktailId = cocktailId,
                        timerViewModel = timerViewModel,
                        showBackButton = false,
                        contentPadding = innerPadding
                    )
                }
            }
        }
    }
}