package com.example.aplikacjabarmanska

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aplikacjabarmanska.screens.CocktailViewModel
import com.example.aplikacjabarmanska.ui.theme.AplikacjaBarmanskaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[CocktailViewModel::class.java]

        setContent {
            AplikacjaBarmanskaTheme {
                CocktailListScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailListScreen(viewModel: CocktailViewModel) {
    val cocktails by viewModel.cocktails.collectAsState()
    val context = LocalContext.current

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
                        .clickable {
                            val intent = Intent(context, com.example.aplikacjabarmanska.screens.DetailActivity::class.java)
                            intent.putExtra("cocktailId", cocktail.id)
                            context.startActivity(intent)
                        }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
