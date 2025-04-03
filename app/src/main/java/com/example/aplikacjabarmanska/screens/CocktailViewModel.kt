package com.example.aplikacjabarmanska.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikacjabarmanska.model.Cocktail
import com.example.aplikacjabarmanska.model.CocktailDatabase
import com.example.aplikacjabarmanska.model.CocktailRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CocktailViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: CocktailRepository
    val cocktails: StateFlow<List<Cocktail>>

    init {
        val db = CocktailDatabase.getDatabase(application)
        repo = CocktailRepository(db.cocktailDao())

        cocktails = repo.observeCocktails()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

        viewModelScope.launch {
            repo.resetAndInsertData()
        }
    }

}