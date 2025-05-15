package com.example.aplikacjabarmanska

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import com.example.aplikacjabarmanska.screens.CocktailDetailScreenWithTimer
import com.example.aplikacjabarmanska.screens.CocktailListScreen
import com.example.aplikacjabarmanska.screens.CocktailViewModel
import com.example.aplikacjabarmanska.ui.theme.AplikacjaBarmanskaTheme
import com.example.aplikacjabarmanska.screens.TimerViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.aplikacjabarmanska.screens.CategorySelectionScreen
import androidx.activity.compose.BackHandler
import com.example.aplikacjabarmanska.data.ThemeManager
import com.example.aplikacjabarmanska.data.InitializeSystemTheme
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.tween

class MainActivity : ComponentActivity() {

    private lateinit var timerViewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[CocktailViewModel::class.java]

        timerViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[TimerViewModel::class.java]

        setContent {
            // Inicjalizacja z ustawieniami systemowymi
            InitializeSystemTheme()

            AplikacjaBarmanskaTheme {
                val tablet = isTablet()
                var selectedId by rememberSaveable { mutableStateOf<Int?>(null) }
                var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }

                LaunchedEffect(selectedId) {
                    selectedId?.let { id  ->
                        timerViewModel.setCocktailId(id)
                    }
                }

                BackHandler(enabled = selectedCategory != null) {
                    selectedCategory = null
                    viewModel.selectCategory(null)
                    selectedId = null
                }

                if (tablet) {
                    if (selectedCategory == null) {
                        // Gdy nie wybrano kategorii, pokazuj ekran kategorii na całej szerokości
                        CategorySelectionScreen(
                            onCategorySelected = { category ->
                                selectedCategory = category
                                viewModel.selectCategory(category)
                            }
                        )
                    } else {
                        // Po wybraniu kategorii, pokazuj standardowy układ z podziałem
                        var localSelectedId by remember { mutableStateOf(selectedId) }
                        var isContentReady by remember { mutableStateOf(false) }

                        // Pobranie pierwszego ID z listy przy zmianie kategorii
                        val cocktails by viewModel.filteredCocktails.collectAsState()

                        // Automatycznie wybierz pierwszy koktajl z listy, jeśli żaden nie jest wybrany
                        LaunchedEffect(cocktails) {
                            if (cocktails.isNotEmpty()) {
                                if (localSelectedId == null) {
                                    localSelectedId = cocktails[0].id
                                    selectedId = cocktails[0].id
                                    timerViewModel.setCocktailId(cocktails[0].id)
                                }
                                // Krótkie opóźnienie dla upewnienia się, że dane zostały załadowane
                                kotlinx.coroutines.delay(50)
                                isContentReady = true
                            }
                        }

                        // Pokazuj zawartość dopiero, gdy wszystko jest gotowe
                        androidx.compose.animation.AnimatedVisibility(
                            visible = isContentReady,
                            enter = androidx.compose.animation.fadeIn(
                                initialAlpha = 0.3f,
                                animationSpec = androidx.compose.animation.core.tween(200)
                            )
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                Box(modifier = Modifier.weight(1f)) {
                                    CocktailListScreen(
                                        viewModel = viewModel,
                                        onCocktailSelected = { id ->
                                            selectedId = id
                                            localSelectedId = id
                                        },
                                        onBackToCategories = {
                                            selectedCategory = null
                                            viewModel.selectCategory(null)
                                            selectedId = null // Resetuj również wybrany koktajl
                                            localSelectedId = null
                                            isContentReady = false
                                        }
                                    )
                                }
                                Box(modifier = Modifier.weight(2f).padding(16.dp)) {
                                    localSelectedId?.let {
                                        CocktailDetailScreenWithTimer(cocktailId = it, timerViewModel = timerViewModel)
                                    }
                                }
                            }
                        }

                        // Wskaźnik ładowania, gdy zawartość nie jest jeszcze gotowa
                        if (!isContentReady) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                } else {
                    // Kod dla telefonu
                    if (selectedCategory == null) {
                        CategorySelectionScreen(
                            onCategorySelected = { category ->
                                selectedCategory = category
                                viewModel.selectCategory(category)
                            }
                        )
                    } else {
                        CocktailListScreen(
                            viewModel = viewModel,
                            onCocktailSelected = { id ->
                                val intent = Intent(this, com.example.aplikacjabarmanska.screens.DetailActivity::class.java)
                                intent.putExtra("cocktailId", id)
                                startActivity(intent)
                            },
                            onBackToCategories = {
                                selectedCategory = null
                                viewModel.selectCategory(null)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp >= 600
}