package com.example.aplikacjabarmanska.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import com.example.aplikacjabarmanska.data.ThemeManager
import com.example.aplikacjabarmanska.ui.theme.LightSurface
import com.example.aplikacjabarmanska.ui.theme.DarkSurfaceContainer

/**
 * Komponent do wyświetlania listy koktajli bez własnego TopAppBar
 * Używany w trybie tabletu, gdy chcemy mieć jeden wspólny nagłówek
 */
@Composable
fun CocktailListScreenContent(
    viewModel: CocktailViewModel,
    onCocktailSelected: (Int) -> Unit
) {
    val cocktails by viewModel.filteredCocktails.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // ThemeManager do kontroli trybu ciemnego
    val themeManager = ThemeManager.getInstance()
    val isDarkMode by themeManager.isDarkTheme.collectAsState()

    if (cocktails.isEmpty() && searchQuery.isNotEmpty()) {
        // Komunikat o braku wyników
        Box(
            modifier = Modifier.fillMaxSize(),
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
            modifier = Modifier.fillMaxSize()
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