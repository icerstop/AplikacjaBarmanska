package com.example.aplikacjabarmanska.model
import kotlinx.coroutines.flow.Flow
import com.example.aplikacjabarmanska.R

class CocktailRepository(private val dao: CocktailDao) {
    fun observeCocktails(): Flow<List<Cocktail>> = dao.observeAll()
    suspend fun getCocktails(): List<Cocktail> = dao.getAll()

    suspend fun insertInitialData() {
        val sample = listOf(
            // DRINKI ALKOHOLOWE
            Cocktail(name = "MOJITO", ingredients = "Rum, limonka, mięta, cukier, soda", instructions = "Rozgnieć miętę z cukrem, dodaj rum i sodę.", imageResId = R.drawable.mojito, category = "drink"),
            Cocktail(name = "COSMOPOLITAN", ingredients = "Wódka, Cointreau, żurawina, limonka", instructions = "Wstrząśnij w shakerze i podaj w kieliszku.", imageResId = R.drawable.cosmopolitan, category = "drink"),
            Cocktail(name = "MARGARITA", ingredients = "Tequila, likier pomarańczowy, sok z limonki", instructions = "Wstrząśnij w shakerze i podaj z solą.", imageResId = R.drawable.margarita, category = "drink"),
            Cocktail(name = "PINA COLADA", ingredients = "Rum, mleko kokosowe, sok ananasowy", instructions = "Wstrząśnij w shakerze i podaj z ananasem.", imageResId = R.drawable.pinacolada, category = "drink"),
            Cocktail(name = "OLD FASHIONED", ingredients = "Bourbon, cukier, angostura, woda", instructions = "Rozpuść cukier z bitters i wodą, dodaj bourbon i lód.", imageResId = R.drawable.oldfashioned, category = "drink"),
            Cocktail(name = "NEGRONI", ingredients = "Gin, Campari, słodki wermut", instructions = "Wymieszaj z lodem i podaj z plasterkiem pomarańczy.", imageResId = R.drawable.negroni, category = "drink"),
            Cocktail(name = "WHISKEY SOUR", ingredients = "Whiskey, sok z cytryny, cukier", instructions = "Wstrząśnij w shakerze z lodem i przecedź do szklanki.", imageResId = R.drawable.whiskysour, category = "drink"),
            Cocktail(name = "DAIQUIRI", ingredients = "Rum, sok z limonki, cukier", instructions = "Wstrząśnij w shakerze z lodem i przecedź.", imageResId = R.drawable.daiquiri, category = "drink"),
            Cocktail(name = "MANHATTAN", ingredients = "Whiskey, słodki wermut, angostura", instructions = "Wymieszaj z lodem i podaj w kieliszku koktajlowym.", imageResId = R.drawable.manhattan, category = "drink"),
            Cocktail(name = "APEROL SPRITZ", ingredients = "Aperol, prosecco, woda gazowana", instructions = "Wlej wszystko do kieliszka z lodem i delikatnie wymieszaj.", imageResId = R.drawable.aperolspritz, category = "drink"),
            Cocktail(name = "LONG ISLAND ICED TEA", ingredients = "Wódka, gin, rum, tequila, triple sec, sok z cytryny, cola", instructions = "Wymieszaj składniki z lodem i dolej coli.", imageResId = R.drawable.longislandicedtea, category = "drink"),
            Cocktail(name = "CAIPIRINHA", ingredients = "Cachaça, limonka, cukier", instructions = "Ugnieć limonkę z cukrem i dodaj cachaçę z lodem.", imageResId = R.drawable.caipirinha, category = "drink"),
            Cocktail(name = "JAGER BOMB", ingredients = "Jagermeister, Red Bull", instructions = "Upuść kieliszek Jagera do szklanki z Red Bullem.", imageResId = R.drawable.jager_bomb, category = "drink"),


            // SHOTY
            Cocktail(name = "KAMIKAZE", ingredients = "Wódka, blue curacao, sok z limonki", instructions = "Wstrząśnij wszystkie składniki z lodem i przecedź.", imageResId = R.drawable.kamikaze, category = "shot"),
            Cocktail(name = "B-52", ingredients = "Kahlua, Baileys, Grand Marnier", instructions = "Delikatnie warstwuj składniki w kieliszku.", imageResId = R.drawable.b52, category = "shot"),
            Cocktail(name = "WŚCIEKŁY PIES", ingredients = "Wódka, syrop malinowy, tabasco", instructions = "Wlej wódkę, dodaj syrop i kilka kropel tabasco.", imageResId = R.drawable.wsciekly_pies, category = "shot"),
            Cocktail(name = "TEQUILA SHOT", ingredients = "Tequila, sól, limonka", instructions = "Poliż sól, wypij tequilę, ugryź limonkę.", imageResId = R.drawable.tequila_shot, category = "shot"),
            Cocktail(name = "LEMON DROP", ingredients = "Wódka cytrynowa, sok z cytryny, cukier", instructions = "Wstrząśnij składniki z lodem, podaj w kieliszku z cukrem na brzegu.", imageResId = R.drawable.lemon_drop, category = "shot"),
            Cocktail(name = "BUTTERY NIPPLE", ingredients = "Butterscotch schnapps, Irish cream", instructions = "Warstwuj składniki w kieliszku shot.", imageResId = R.drawable.buttery_nipple, category = "shot"),
            Cocktail(name = "SAMBUCA", ingredients = "Sambuca, ziarna kawy", instructions = "Podaj sambucę z trzema ziarnami kawy.", imageResId = R.drawable.sambuca, category = "shot"),

            // DRINKI BEZALKOHOLOWE
            Cocktail(name = "VIRGIN MOJITO", ingredients = "Limonka, mięta, cukier trzcinowy, soda", instructions = "Rozgnieć miętę z cukrem i limonką, dodaj lód i sodę.", imageResId = R.drawable.mojito, category = "soft"),
            Cocktail(name = "SHIRLEY TEMPLE", ingredients = "Ginger ale, syrop grenadynowy, wisienka koktajlowa", instructions = "Wlej ginger ale do szklanki z lodem, dodaj syrop grenadynowy i udekoruj wisienką.", imageResId = R.drawable.shirley_temple, category = "soft"),
            Cocktail(name = "VIRGIN COLADA", ingredients = "Sok ananasowy, mleko kokosowe, syrop cukrowy", instructions = "Zmiksuj wszystkie składniki z lodem, podaj z kawałkiem ananasa.", imageResId = R.drawable.pinacolada, category = "soft"),
            Cocktail(name = "FRUIT PUNCH", ingredients = "Sok pomarańczowy, sok ananasowy, grenadyna, soda", instructions = "Wymieszaj soki z grenadyną, dodaj sodę i lód.", imageResId = R.drawable.fruit_punch, category = "soft"),
            Cocktail(name = "CINDERELLA", ingredients = "Sok pomarańczowy, sok ananasowy, sok cytrynowy, grenadyna", instructions = "Wstrząśnij wszystkie składniki z lodem i przecedź do szklanki.", imageResId = R.drawable.cinderella, category = "soft"),
            Cocktail(name = "VIRGIN STRAWBERRY DAIQUIRI", ingredients = "Truskawki, sok z limonki, cukier, lód", instructions = "Zmiksuj wszystkie składniki do uzyskania gładkiej konsystencji.", imageResId = R.drawable.virgin_strawberry_daiquiri, category = "soft"),
            Cocktail(name = "ARNOLD PALMER", ingredients = "Mrożona herbata, lemoniada", instructions = "Wymieszaj w równych proporcjach mrożoną herbatę i lemoniadę.", imageResId = R.drawable.arnold_palmer, category = "soft"),
            Cocktail(name = "BERRY FIZZ", ingredients = "Syrop malinowy, sok z cytryny, soda, świeże owoce", instructions = "Wymieszaj syrop z sokiem cytrynowym, dodaj lód i sodę, udekoruj owocami.", imageResId = R.drawable.berry_fizz, category = "soft"),
            Cocktail(name = "CUCUMBER COOLER", ingredients = "Ogórek, sok z limonki, mięta, soda, syrop cukrowy", instructions = "Zmiksuj ogórek z miętą i sokiem z limonki, przecedź, dodaj syrop i sodę.", imageResId = R.drawable.cucumber_cooler, category = "soft"),
            Cocktail(name = "TROPICAL SUNRISE", ingredients = "Sok pomarańczowy, sok z mango, grenadyna", instructions = "Wlej sok pomarańczowy i z mango do szklanki, delikatnie dodaj grenadynę na dno.", imageResId = R.drawable.tropical_sunrise, category = "soft")
        )
        dao.insertAll(sample)
    }

    suspend fun resetAndInsertData() {
        dao.deleteAll()
        insertInitialData()
    }
}