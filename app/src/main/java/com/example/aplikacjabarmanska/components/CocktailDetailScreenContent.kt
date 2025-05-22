package com.example.aplikacjabarmanska.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplikacjabarmanska.components.CocktailTimer
import com.example.aplikacjabarmanska.data.ThemeManager
import com.example.aplikacjabarmanska.isTablet
import com.example.aplikacjabarmanska.model.Cocktail
import com.example.aplikacjabarmanska.model.CocktailDatabase

/**
 * Komponent do wyświetlania szczegółów koktajlu
 */
@Composable
fun CocktailDetailScreenContent(
    cocktailId: Int,
    timerViewModel: TimerViewModel,
    showBackButton: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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

    if (cocktail != null) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding) // Używamy przekazanego paddingu
                    .verticalScroll(scrollState)
            ) {
                // Zdjęcie koktajlu - zaokrąglone i widoczne w całości
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode)
                            Color(0xFF2D2D2D)
                        else
                            Color(0xFFFFFFFF)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = cocktail!!.imageResId),
                            contentDescription = cocktail!!.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )

                        // Gradient overlay na dole obrazka dla lepszej czytelności
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.3f)
                                        )
                                    )
                                )
                        )
                    }
                }

                // Główna zawartość
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Sekcja składników z ikoną
                    SectionCard(
                        title = "Składniki",
                        icon = Icons.Outlined.Restaurant,
                        content = cocktail!!.ingredients,
                        isDarkMode = isDarkMode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sekcja przygotowania z ikoną
                    SectionCard(
                        title = "Sposób przygotowania",
                        icon = Icons.Outlined.AccessTime,
                        content = cocktail!!.instructions,
                        isDarkMode = isDarkMode
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Minutnik z lepszym designem
                    EnhancedTimerCard(
                        formattedTime = formattedTime,
                        isRunning = isRunning,
                        onStart = { seconds -> timerViewModel.startTimer(seconds) },
                        onStop = { timerViewModel.stopTimer() },
                        onReset = { timerViewModel.resetTimer() },
                        isDarkMode = isDarkMode
                    )

                    Spacer(modifier = Modifier.height(100.dp)) // Miejsce na FAB
                }
            }

            // FAB
            FloatingActionButton(
                onClick = { shareIngredients(context, cocktail!!) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(contentPadding.calculateBottomPadding()) // Dodatkowy padding dla FAB
                    .shadow(8.dp, CircleShape)
            ) {
                Icon(Icons.Default.Share, contentDescription = "Udostępnij składniki")
            }
        }
    } else {
        // Loading state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: String,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode)
                Color(0xFF2D2D2D)
            else
                Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Nagłówek z ikoną
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Zawartość
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun EnhancedTimerCard(
    formattedTime: String,
    isRunning: Boolean,
    onStart: (Long) -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode)
                Color(0xFF2D2D2D)
            else
                Color(0xFFFFFFFF)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nagłówek minutnika
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = "Minutnik",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Użyj oryginalnego komponentu CocktailTimer
            CocktailTimer(
                formattedTime = formattedTime,
                isRunning = isRunning,
                onStart = onStart,
                onStop = onStop,
                onReset = onReset
            )
        }
    }
}

private fun shareIngredients(context: Context, cocktail: Cocktail) {
    val message = """
        🍹 ${cocktail.name}
        
        📋 Składniki:
        ${cocktail.ingredients}
        
        👨‍🍳 Sposób przygotowania:
        ${cocktail.instructions}
        
        Przygotowane z aplikacją Barmańską 🍸
    """.trimIndent()

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Udostępnij przepis na ${cocktail.name}")
    context.startActivity(shareIntent)
}