package com.example.aplikacjabarmanska.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.aplikacjabarmanska.model.Cocktail
import com.example.aplikacjabarmanska.model.CocktailDatabase
import com.example.aplikacjabarmanska.ui.theme.AplikacjaBarmanskaTheme
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip


class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cocktailId = intent.getIntExtra("cocktailId", -1)

        setContent {
            AplikacjaBarmanskaTheme {
                CocktailDetailScreen(cocktailId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailScreen(cocktailId: Int) {
    val context = LocalContext.current
    var cocktail by remember { mutableStateOf<Cocktail?>(null) }

    LaunchedEffect(cocktailId) {
        val db = CocktailDatabase.getDatabase(context)
        cocktail = db.cocktailDao().getAll().find { it.id == cocktailId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        cocktail?.name ?: "Szczegóły koktajlu",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xffe6e2dc),
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color(0xffe6e2dc)
    ) { padding ->
        if (cocktail != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Obraz drinka
                Image(
                    painter = painterResource(id = cocktail!!.imageResId),
                    contentDescription = cocktail!!.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tekstowe informacje
                Text("Składniki:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(cocktail!!.ingredients, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(12.dp))
                Text("Sposób przygotowania:\n${cocktail!!.instructions}")
            }
        } else {
            Text("Nie znaleziono koktajlu", modifier = Modifier.padding(16.dp))
        }
    }
}
