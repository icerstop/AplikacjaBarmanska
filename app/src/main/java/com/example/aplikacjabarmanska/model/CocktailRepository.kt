package com.example.aplikacjabarmanska.model
import kotlinx.coroutines.flow.Flow

class CocktailRepository(private val dao: CocktailDao) {
    fun observeCocktails(): Flow<List<Cocktail>> = dao.observeAll()
    suspend fun getCocktails(): List<Cocktail> = dao.getAll()

    suspend fun insertInitialData() {
        val sample = listOf(
            Cocktail(name = "MOJITO", ingredients = "Rum, limonka, mięta, cukier, soda", instructions = "Rozgnieć miętę z cukrem, dodaj rum i sodę."),
            Cocktail(name = "COSMOPOLITAN", ingredients = "Wódka, Cointreau, żurawina, limonka", instructions = "Wstrząśnij w shakerze i podaj w kieliszku."),
            Cocktail(name = "MARGARITA123", ingredients = "Tequila, likier pomarańczowy, sok z limonki", instructions = "Wstrząśnij w shakerze i podaj z solą."),
            Cocktail(name = "PINA COLADA", ingredients = "Rum, mleko kokosowe, sok ananasowy", instructions = "Wstrząśnij w shakerze i podaj z ananasem.")
        )
        dao.insertAll(sample)
    }

    suspend fun resetAndInsertData() {
        dao.deleteAll()
        insertInitialData()
    }
}
