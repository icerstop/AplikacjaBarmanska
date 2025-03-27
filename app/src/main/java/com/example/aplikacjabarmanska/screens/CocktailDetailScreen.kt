package com.example.aplikacjabarmanska

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aplikacjabarmanska.model.sampleCocktails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailScreen(cocktailId: Int) {
    val cocktail = sampleCocktails().find { it.id == cocktailId }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(cocktail?.name ?: "Szczegóły koktajlu") })
        }
    ) { padding ->
        if (cocktail != null) {
            Column(modifier = Modifier
                .padding(padding)
                .padding(20.dp)) {
                Text("Składniki:", style = MaterialTheme.typography.titleMedium)
                Text(cocktail.ingredients)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Sposób przygotowania:", style = MaterialTheme.typography.titleMedium)
                Text(cocktail.instructions)
            }
        } else {
            Text(
                "Nie znaleziono koktajlu.",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
