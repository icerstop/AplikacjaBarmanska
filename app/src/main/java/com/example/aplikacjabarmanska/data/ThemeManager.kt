package com.example.aplikacjabarmanska.data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton do zarządzania trybem ciemnym aplikacji
 */
class ThemeManager private constructor() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemeManager? = null

        fun getInstance(): ThemeManager {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemeManager()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Funkcja Composable do inicjalizacji trybu ciemnego na podstawie ustawień systemowych
 */
@Composable
fun InitializeSystemTheme() {
    val themeManager = remember { ThemeManager.getInstance() }
    val systemDarkTheme = isSystemInDarkTheme()

    // Używamy LaunchedEffect zamiast bezpośredniego dostępu do .value
    LaunchedEffect(systemDarkTheme) {
        themeManager.setDarkTheme(systemDarkTheme)
    }
}