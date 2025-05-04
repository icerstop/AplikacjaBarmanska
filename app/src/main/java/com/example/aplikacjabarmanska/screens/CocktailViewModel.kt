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
    private val _cocktails = MutableStateFlow<List<Cocktail>>(emptyList())
    val cocktails: StateFlow<List<Cocktail>> = _cocktails.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    val filteredCocktails: StateFlow<List<Cocktail>> = combine(
        _selectedCategory,
        _cocktails,
        _searchQuery
    ) { category, allCocktails, query ->
        val categoryFiltered = when (category) {
            "drink" -> allCocktails.filter { it.category == "drink" }
            "shot" -> allCocktails.filter { it.category == "shot" }
            "soft" -> allCocktails.filter { it.category == "soft" }
            else -> allCocktails
        }

        if (query.isEmpty()) {
            categoryFiltered
        } else {
            categoryFiltered.filter { cocktail ->
                cocktail.name.contains(query, ignoreCase = true) ||
                        cocktail.ingredients.contains(query, ignoreCase = true) ||
                        cocktail.instructions.contains(query, ignoreCase = true)
            }
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        val db = CocktailDatabase.getDatabase(application)
        repo = CocktailRepository(db.cocktailDao())

        viewModelScope.launch {
            repo.observeCocktails().collect { cocktailList ->
                _cocktails.value = cocktailList
            }
        }

        viewModelScope.launch {
            repo.resetAndInsertData()
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearching(isSearching: Boolean) {
        _isSearching.value = isSearching
        if (!isSearching) {
            _searchQuery.value = ""
        }
    }
}