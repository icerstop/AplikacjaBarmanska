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

                LaunchedEffect(selectedId) {
                    selectedId?.let { id  ->
                        timerViewModel.setCocktailId(id)
                    }
                }

                if (tablet) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.weight(1f)) {
                            CocktailListScreen(viewModel = viewModel) { id -> selectedId = id }
                        }
                        Box(modifier = Modifier.weight(2f).padding(16.dp)) {
                            selectedId?.let {
                                CocktailDetailScreenWithTimer(cocktailId = it, timerViewModel = timerViewModel)
                            }
                        }
                    }
                } else {
                    CocktailListScreen(viewModel = viewModel) { id ->
                        val intent = Intent(this, com.example.aplikacjabarmanska.screens.DetailActivity::class.java)
                        intent.putExtra("cocktailId", id)
                        startActivity(intent)
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
