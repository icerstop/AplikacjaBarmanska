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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import com.example.aplikacjabarmanska.isTablet
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.aplikacjabarmanska.components.CocktailTimer
import androidx.lifecycle.ViewModelProvider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import android.content.Intent
import android.content.Context


class DetailActivity : ComponentActivity() {
    private lateinit var timerViewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isTablet = resources.configuration.screenWidthDp >= 600
        if (isTablet) {
            finish()
            return
        }

        val cocktailId = intent.getIntExtra("cocktailId", -1)

        timerViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[TimerViewModel::class.java]

        timerViewModel.setCocktailId(cocktailId)

        setContent {
            AplikacjaBarmanskaTheme {
                CocktailDetailScreenWithTimer(cocktailId, timerViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailScreenWithTimer(cocktailId: Int, timerViewModel: TimerViewModel) {
    val context = LocalContext.current
    val isTablet = isTablet()
    var cocktail by remember { mutableStateOf<Cocktail?>(null) }
    val scrollState = rememberScrollState()

    val formattedTime by timerViewModel.formattedTime.collectAsState()
    val isRunning by timerViewModel.isRunning.collectAsState()

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
                navigationIcon = {
                    if(!isTablet){
                        IconButton(onClick = { (context as? ComponentActivity)?.finish()}){
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Wróć",
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xffc6bbae),
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            cocktail?.let {
                FloatingActionButton(
                    onClick = { shareIngredients(context, it)},
                    containerColor = Color(0xFF988B78),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Udostępnij składniki")
                }
            }
        },
        containerColor = Color(0xffe6e2dc)
    ) { padding ->
        if (cocktail != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
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

                Spacer(modifier = Modifier.height(24.dp))

                CocktailTimer(
                    formattedTime = formattedTime,
                    isRunning = isRunning,
                    onStart = { seconds -> timerViewModel.startTimer(seconds) },
                    onStop = { timerViewModel.stopTimer() },
                    onReset = { timerViewModel.resetTimer()}
                )
            }
        } else {
            Text("Nie znaleziono koktajlu", modifier = Modifier.padding(16.dp))
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
