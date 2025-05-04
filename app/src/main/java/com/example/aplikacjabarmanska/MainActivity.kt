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
                        CategorySelectionScreen { category ->
                            selectedCategory = category
                            viewModel.selectCategory(category)
                        }
                    } else {
                        // Po wybraniu kategorii, pokazuj standardowy układ z podziałem
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier.weight(1f)) {
                                CocktailListScreen(
                                    viewModel = viewModel,
                                    onCocktailSelected = { id -> selectedId = id },
                                    onBackToCategories = {
                                        selectedCategory = null
                                        viewModel.selectCategory(null)
                                        selectedId = null // Resetuj również wybrany koktajl
                                    }
                                )
                            }
                            Box(modifier = Modifier.weight(2f).padding(16.dp)) {
                                selectedId?.let {
                                    CocktailDetailScreenWithTimer(cocktailId = it, timerViewModel = timerViewModel)
                                }
                            }
                        }
                    }
                } else {
                    // Kod dla telefonu
                    if (selectedCategory == null) {
                        CategorySelectionScreen { category ->
                            selectedCategory = category
                            viewModel.selectCategory(category)
                        }
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