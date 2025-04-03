package com.example.aplikacjabarmanska.model
import kotlinx.coroutines.flow.Flow
import com.example.aplikacjabarmanska.R


class CocktailRepository(private val dao: CocktailDao) {
    fun observeCocktails(): Flow<List<Cocktail>> = dao.observeAll()
    suspend fun getCocktails(): List<Cocktail> = dao.getAll()

    suspend fun insertInitialData() {
        val sample = listOf(
            Cocktail(name = "MOJITO", ingredients = "Rum, limonka, mięta, cukier, soda", instructions = "Rozgnieć miętę z cukrem, dodaj rum i sodę.", imageResId = R.drawable.mojito),
            Cocktail(name = "COSMOPOLITAN", ingredients = "Wódka, Cointreau, żurawina, limonka", instructions = "Wstrząśnij w shakerze i podaj w kieliszku.", imageResId = R.drawable.cosmopolitan),
            Cocktail(name = "MARGARITA", ingredients = "Tequila, likier pomarańczowy, sok z limonki", instructions = "Wstrząśnij w shakerze i podaj z solą.", imageResId = R.drawable.margarita),
            Cocktail(name = "PINA COLADA", ingredients = "Rum, mleko kokosowe, sok ananasowy", instructions = "Wstrząśnij w shakerze i podaj z ananasem.", imageResId = R.drawable.pinacolada),
            Cocktail(name = "OLD FASHIONED", ingredients = "Bourbon, cukier, angostura, woda", instructions = "Rozpuść cukier z bitters i wodą, dodaj bourbon i lód.", imageResId = R.drawable.oldfashioned),
            Cocktail(name = "NEGRONI", ingredients = "Gin, Campari, słodki wermut", instructions = "Wymieszaj z lodem i podaj z plasterkiem pomarańczy.", imageResId = R.drawable.negroni),
            Cocktail(name = "WHISKEY SOUR", ingredients = "Whiskey, sok z cytryny, cukier", instructions = "Wstrząśnij w shakerze z lodem i przecedź do szklanki.", imageResId = R.drawable.whiskysour),
            Cocktail(name = "DAIQUIRI", ingredients = "Rum, sok z limonki, cukier", instructions = "Wstrząśnij w shakerze z lodem i przecedź.", imageResId = R.drawable.daiquiri),
            Cocktail(name = "MANHATTAN", ingredients = "Whiskey, słodki wermut, angostura", instructions = "Wymieszaj z lodem i podaj w kieliszku koktajlowym.", imageResId = R.drawable.manhattan),
            Cocktail(name = "APEROL SPRITZ", ingredients = "Aperol, prosecco, woda gazowana", instructions = "Wlej wszystko do kieliszka z lodem i delikatnie wymieszaj.", imageResId = R.drawable.aperolspritz),
            Cocktail(name = "LONG ISLAND ICED TEA", ingredients = "Wódka, gin, rum, tequila, triple sec, sok z cytryny, cola", instructions = "Wymieszaj składniki z lodem i dolej coli.", imageResId = R.drawable.longislandicedtea),
            Cocktail(name = "CAIPIRINHA", ingredients = "Cachaça, limonka, cukier", instructions = "Ugnieć limonkę z cukrem i dodaj cachaçę z lodem.", imageResId = R.drawable.caipirinha)
        )
        dao.insertAll(sample)
    }

    suspend fun resetAndInsertData() {
        dao.deleteAll()
        insertInitialData()
    }
}
