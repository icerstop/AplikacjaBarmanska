package com.example.aplikacjabarmanska.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import com.example.aplikacjabarmanska.data.ThemeManager
import com.example.aplikacjabarmanska.ui.theme.LightAppBarColor
import com.example.aplikacjabarmanska.ui.theme.DarkAppBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionScreen(
    onCategorySelected: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    // ThemeManager do kontroli trybu ciemnego
    val themeManager = ThemeManager.getInstance()
    val isDarkMode by themeManager.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wybierz kategorię") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDarkMode) DarkAppBarColor else LightAppBarColor,
                    titleContentColor = if (isDarkMode) Color.White else Color.Black
                ),
                actions = {
                    // Przełącznik trybu ciemnego
                    IconButton(onClick = { themeManager.toggleTheme() }) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Przełącz na tryb jasny" else "Przełącz na tryb ciemny",
                            tint = if (isDarkMode) Color.White else Color.Black
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Co chcesz przygotować?",
                fontSize = if (isTablet) 32.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = if (isTablet) 48.dp else 32.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            if (isTablet) {
                // Układ dla tabletu - przyciski obok siebie
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Przycisk dla drinków
                    Button(
                        onClick = { onCategorySelected("drink") },
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6650a4)
                        )
                    ) {
                        Text(
                            text = "DRINKI",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Przycisk dla shotów
                    Button(
                        onClick = { onCategorySelected("shot") },
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7D5260)
                        )
                    ) {
                        Text(
                            text = "SHOTY",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Przycisk dla drinków bezalkoholowych
                    Button(
                        onClick = { onCategorySelected("soft") },
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text(
                            text = "BEZALKOHOLOWE",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Układ dla telefonu - przyciski jeden pod drugim
                // Przycisk dla drinków
                Button(
                    onClick = { onCategorySelected("drink") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                        .height(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6650a4)
                    )
                ) {
                    Text(
                        text = "DRINKI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Przycisk dla shotów
                Button(
                    onClick = { onCategorySelected("shot") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                        .height(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7D5260)
                    )
                ) {
                    Text(
                        text = "SHOTY",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Przycisk dla drinków bezalkoholowych
                Button(
                    onClick = { onCategorySelected("soft") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                        .height(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(
                        text = "BEZALKOHOLOWE",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}