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
import com.example.aplikacjabarmanska.screens.CocktailDetailScreenContent
import com.example.aplikacjabarmanska.screens.CocktailListScreenContent
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
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.example.aplikacjabarmanska.ui.theme.DarkAppBarColor
import com.example.aplikacjabarmanska.ui.theme.LightAppBarColor

class MainActivity : ComponentActivity() {

    private lateinit var timerViewModel: TimerViewModel

    @OptIn(ExperimentalMaterial3Api::class)
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
                    // Animacje dla widoku wyboru kategorii
                    val enterCategoryScreen = fadeIn(
                        animationSpec = tween(durationMillis = 500)
                    ) + slideInHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        initialOffsetX = { fullWidth -> -fullWidth / 3 }
                    )

                    val exitCategoryScreen = fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    ) + slideOutHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )

                    // Animacje dla widoku listy koktajli
                    val enterCocktailScreen = fadeIn(
                        animationSpec = tween(durationMillis = 500)
                    ) + slideInHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        initialOffsetX = { fullWidth -> fullWidth / 3 }
                    )

                    val exitCocktailScreen = fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    ) + slideOutHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        targetOffsetX = { fullWidth -> fullWidth }
                    )

                    AnimatedVisibility(
                        visible = selectedCategory == null,
                        enter = enterCategoryScreen,
                        exit = exitCategoryScreen
                    ) {
                        // Gdy nie wybrano kategorii, pokazuj ekran kategorii na całej szerokości
                        CategorySelectionScreen(
                            onCategorySelected = { category ->
                                selectedCategory = category
                                viewModel.selectCategory(category)
                            }
                        )
                    }

                    AnimatedVisibility(
                        visible = selectedCategory != null,
                        enter = enterCocktailScreen,
                        exit = exitCocktailScreen
                    ) {
                        // Po wybraniu kategorii, pokazuj standardowy układ z podziałem
                        var localSelectedId by remember { mutableStateOf(selectedId) }
                        var isContentReady by remember { mutableStateOf(false) }

                        // Pobranie pierwszego ID z listy przy zmianie kategorii
                        val cocktails by viewModel.filteredCocktails.collectAsState()
                        val isSearching by viewModel.isSearching.collectAsState()
                        val searchQuery by viewModel.searchQuery.collectAsState()
                        val focusManager = LocalFocusManager.current

                        // ThemeManager do kontroli trybu ciemnego
                        val themeManager = ThemeManager.getInstance()
                        val isDarkMode by themeManager.isDarkTheme.collectAsState()

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

                        // Używaj jednego wspólnego Scaffold z jednym TopAppBar dla całego widoku
                        Scaffold(
                            topBar = {
                                if (isSearching) {
                                    // Pasek wyszukiwania
                                    TopAppBar(
                                        title = {
                                            TextField(
                                                value = searchQuery,
                                                onValueChange = { viewModel.updateSearchQuery(it) },
                                                placeholder = { Text("Szukaj w nazwie lub składnikach...") },
                                                singleLine = true,
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = Color.Transparent,
                                                    unfocusedContainerColor = Color.Transparent,
                                                    disabledContainerColor = Color.Transparent,
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent,
                                                ),
                                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                                keyboardActions = KeyboardActions(
                                                    onSearch = { focusManager.clearFocus() }
                                                )
                                            )
                                        },
                                        navigationIcon = {
                                            IconButton(onClick = {
                                                viewModel.setSearching(false)
                                                focusManager.clearFocus()
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowBack,
                                                    contentDescription = "Zamknij wyszukiwanie"
                                                )
                                            }
                                        },
                                        actions = {
                                            if (searchQuery.isNotEmpty()) {
                                                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Wyczyść"
                                                    )
                                                }
                                            }
                                        },
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor = if (isDarkMode) DarkAppBarColor else LightAppBarColor,
                                            titleContentColor = if (isDarkMode) Color.White else Color.Black
                                        )
                                    )
                                } else {
                                    TopAppBar(
                                        title = {
                                            Text(when(selectedCategory) {
                                                "drink" -> "Drinki"
                                                "shot" -> "Shoty"
                                                "soft" -> "Bezalkoholowe"
                                                else -> "Koktajle"
                                            })
                                        },
                                        navigationIcon = {
                                            IconButton(onClick = {
                                                selectedCategory = null
                                                viewModel.selectCategory(null)
                                                selectedId = null
                                                localSelectedId = null
                                                isContentReady = false
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowBack,
                                                    contentDescription = "Powrót do kategorii",
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

                                            IconButton(onClick = { viewModel.setSearching(true) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "Szukaj"
                                                )
                                            }
                                        },
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor = if (isDarkMode) DarkAppBarColor else LightAppBarColor,
                                            titleContentColor = if (isDarkMode) Color.White else Color.Black
                                        )
                                    )
                                }
                            }
                        ) { padding ->
                            // Pokazuj zawartość dopiero, gdy wszystko jest gotowe
                            AnimatedVisibility(
                                visible = isContentReady,
                                enter = fadeIn(
                                    initialAlpha = 0.1f,
                                    animationSpec = tween(300)
                                ) + slideInHorizontally(
                                    initialOffsetX = { fullWidth -> fullWidth / 5 },
                                    animationSpec = tween(400)
                                )
                            ) {
                                Row(modifier = Modifier.fillMaxSize().padding(padding)) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        // Użyj nowego komponentu bez własnego TopAppBar
                                        CocktailListScreenContent(
                                            viewModel = viewModel,
                                            onCocktailSelected = { id ->
                                                selectedId = id
                                                localSelectedId = id
                                            }
                                        )
                                    }
                                    Box(modifier = Modifier.weight(2f).padding(16.dp)) {
                                        localSelectedId?.let {
                                            // Użyj nowego komponentu bez własnego TopAppBar
                                            CocktailDetailScreenContent(
                                                cocktailId = it,
                                                timerViewModel = timerViewModel,
                                                showBackButton = false
                                            )
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
                    }
                } else {
                    // Kod dla telefonu - z animacją przejścia
                    // Animacje dla widoku wyboru kategorii
                    val enterCategoryScreen = fadeIn(
                        animationSpec = tween(durationMillis = 500)
                    ) + slideInHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        initialOffsetX = { fullWidth -> -fullWidth / 3 }
                    )

                    val exitCategoryScreen = fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    ) + slideOutHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )

                    // Animacje dla widoku listy koktajli
                    val enterCocktailScreen = fadeIn(
                        animationSpec = tween(durationMillis = 500)
                    ) + slideInHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        initialOffsetX = { fullWidth -> fullWidth / 3 }
                    )

                    val exitCocktailScreen = fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    ) + slideOutHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        targetOffsetX = { fullWidth -> fullWidth }
                    )

                    AnimatedVisibility(
                        visible = selectedCategory == null,
                        enter = enterCategoryScreen,
                        exit = exitCategoryScreen
                    ) {
                        CategorySelectionScreen(
                            onCategorySelected = { category ->
                                selectedCategory = category
                                viewModel.selectCategory(category)
                            }
                        )
                    }

                    AnimatedVisibility(
                        visible = selectedCategory != null,
                        enter = enterCocktailScreen,
                        exit = exitCocktailScreen
                    ) {
                        // Tutaj używamy normalnego CocktailListScreen z jego własnym TopAppBar
                        com.example.aplikacjabarmanska.screens.CocktailListScreen(
                            viewModel = viewModel,
                            onCocktailSelected = { id ->
                                val intent = Intent(this@MainActivity, com.example.aplikacjabarmanska.screens.DetailActivity::class.java)
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

// Operator rozszerzenia dla łączenia dwóch animacji wejścia
operator fun EnterTransition.plus(other: EnterTransition): EnterTransition {
    return this + other
}

// Operator rozszerzenia dla łączenia dwóch animacji wyjścia
operator fun ExitTransition.plus(other: ExitTransition): ExitTransition {
    return this + other
}