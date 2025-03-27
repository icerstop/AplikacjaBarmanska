package com.example.aplikacjabarmanska.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import com.example.aplikacjabarmanska.model.Cocktail
import com.example.aplikacjabarmanska.screens.CocktailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailListScreen(
    viewModel: CocktailViewModel,
    onCocktailSelected: (Int) -> Unit
) {
    val cocktails by viewModel.cocktails.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Koktajle") })
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items = cocktails) { cocktail ->
                Text(
                    text = cocktail.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCocktailSelected(cocktail.id) }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

