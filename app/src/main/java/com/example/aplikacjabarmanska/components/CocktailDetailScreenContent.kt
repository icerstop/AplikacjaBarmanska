package com.example.aplikacjabarmanska.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplikacjabarmanska.components.CocktailTimer
import com.example.aplikacjabarmanska.data.ThemeManager
import com.example.aplikacjabarmanska.isTablet
import com.example.aplikacjabarmanska.model.Cocktail
import com.example.aplikacjabarmanska.model.CocktailDatabase

/**
 * Komponent do wyświetlania szczegółów koktajlu bez własnego TopAppBar
 * Używany w trybie tabletu, gdy chcemy mieć jeden wspólny nagłówek
 */
@Composable
fun CocktailDetailScreenContent(
    cocktailId: Int,
    timerViewModel: TimerViewModel,
    showBackButton: Boolean = true
) {
    val context = LocalContext.current
    val isTablet = isTablet()
    var cocktail by remember { mutableStateOf<Cocktail?>(null) }
    val scrollState = rememberScrollState()

    val formattedTime by timerViewModel.formattedTime.collectAsState()
    val isRunning by timerViewModel.isRunning.collectAsState()

    // ThemeManager do kontroli trybu ciemnego
    val themeManager = ThemeManager.getInstance()
    val isDarkMode by themeManager.isDarkTheme.collectAsState()

    LaunchedEffect(cocktailId) {
        val db = CocktailDatabase.getDatabase(context)
        cocktail = db.cocktailDao().getAll().find { it.id == cocktailId }
    }

    Scaffold(
        floatingActionButton = {
            cocktail?.let {
                FloatingActionButton(
                    onClick = { shareIngredients(context, it)},
                    containerColor = if (isDarkMode) MaterialTheme.colorScheme.primary else Color(0xFF988B78),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Udostępnij składniki")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (cocktail != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Wyświetl nazwę koktajlu tylko jeśli jesteśmy w trybie telefonu (nagłówek będzie w TopBar w tablecie)
                if (!isTablet) {
                    Text(
                        text = cocktail!!.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

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
                Text(
                    "Składniki:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    cocktail!!.ingredients,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Sposób przygotowania:\n${cocktail!!.instructions}",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                CocktailTimer(
                    formattedTime = formattedTime,
                    isRunning = isRunning,
                    onStart = { seconds -> timerViewModel.startTimer(seconds) },
                    onStop = { timerViewModel.stopTimer() },
                    onReset = { timerViewModel.resetTimer() }
                )
            }
        } else {
            Text(
                "Nie znaleziono koktajlu",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

private fun shareIngredients(context: Context, cocktail: Cocktail) {
    val message = """
        Skladniki na ${cocktail.name}:
        ${cocktail.ingredients}
        
        Sposób przygotowania:
        ${cocktail.instructions}
    """.trimIndent()

    val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = android.net.Uri.parse("smsto:")
        putExtra("sms_body", message)
    }

    if (smsIntent.resolveActivity(context.packageManager) != null) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val chooserIntent = Intent.createChooser(sendIntent, "Udostępnij składniki przez:")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(smsIntent))

        context.startActivity(chooserIntent)
    } else {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Udostępnij składniki przez:")

        if (shareIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(shareIntent)
        }
    }
}