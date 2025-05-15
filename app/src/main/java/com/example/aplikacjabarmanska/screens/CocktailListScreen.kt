package com.example.aplikacjabarmanska.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.example.aplikacjabarmanska.data.ThemeManager
import com.example.aplikacjabarmanska.ui.theme.LightAppBarColor
import com.example.aplikacjabarmanska.ui.theme.DarkAppBarColor
import com.example.aplikacjabarmanska.ui.theme.LightSurface
import com.example.aplikacjabarmanska.ui.theme.DarkSurfaceContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailListScreen(
    viewModel: CocktailViewModel,
    onCocktailSelected: (Int) -> Unit,
    onBackToCategories: () -> Unit
) {
    val cocktails by viewModel.filteredCocktails.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // ThemeManager do kontroli trybu ciemnego
    val themeManager = ThemeManager.getInstance()
    val isDarkMode by themeManager.isDarkTheme.collectAsState()

    // Automatycznie focus na pole tekstowe gdy włączamy wyszukiwanie
    LaunchedEffect(isSearching) {
        if (isSearching) {
            focusRequester.requestFocus()
        }
    }

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
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
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
                // Normalny pasek aplikacji
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
                        IconButton(onClick = onBackToCategories) {
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
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (cocktails.isEmpty() && searchQuery.isNotEmpty()) {
            // Komunikat o braku wyników
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nie znaleziono koktajli pasujących do \"$searchQuery\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(items = cocktails) { cocktail ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { onCocktailSelected(cocktail.id) },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkMode) DarkSurfaceContainer else LightSurface
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = cocktail.imageResId),
                                contentDescription = cocktail.name,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(96.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Column {
                                Text(
                                    text = cocktail.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cocktail.ingredients.take(40) + "...",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}