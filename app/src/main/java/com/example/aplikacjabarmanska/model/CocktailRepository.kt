package com.example.aplikacjabarmanska.model

import kotlinx.coroutines.flow.Flow
import com.example.aplikacjabarmanska.R

class CocktailRepository(private val dao: CocktailDao) {
    fun observeCocktails(): Flow<List<Cocktail>> = dao.observeAll()
    suspend fun getCocktails(): List<Cocktail> = dao.getAll()

    suspend fun insertInitialData() {
        val sample = listOf(
            // DRINKI ALKOHOLOWE
            Cocktail(
                name = "MOJITO",
                ingredients = "Rum, limonka, mięta, cukier trzcinowy, soda, lód",
                instructions = "1. Umyj i pokrój limonkę na ćwiartki.\n" +
                        "2. Włóż limonkę i liście mięty do szklanki, dosyp cukier trzcinowy.\n" +
                        "3. Delikatnie ugnieć muddlerem, aby uwolnić soki i olejki aromatyczne z mięty.\n" +
                        "4. Dodaj kruszony lód do 3/4 wysokości szklanki.\n" +
                        "5. Wlej 50 ml białego rumu i uzupełnij sodą do pełna.\n" +
                        "6. Zamieszaj delikatnie łyżeczką barmańską i udekoruj gałązką mięty.",
                imageResId = R.drawable.mojito,
                category = "drink"
            ),
            Cocktail(
                name = "COSMOPOLITAN",
                ingredients = "Wódka, Cointreau, sok żurawinowy, sok z limonki, lód",
                instructions = "1. Schłodź kieliszek do koktajli.\n" +
                        "2. W shakerze umieść 40 ml wódki, 15 ml Cointreau, 30 ml soku żurawinowego i 10 ml świeżego soku z limonki z lodem.\n" +
                        "3. Wstrząsaj energicznie przez 15 sekund.\n" +
                        "4. Przecedź do kieliszka, udekoruj skórką z limonki.",
                imageResId = R.drawable.cosmopolitan,
                category = "drink"
            ),
            Cocktail(
                name = "MARGARITA",
                ingredients = "Tequila, likier pomarańczowy, sok z limonki, sól, lód",
                instructions = "1. Zwilż brzeg kieliszka limonką i obtocz w soli.\n" +
                        "2. W shakerze wymieszaj 40 ml tequili, 20 ml likieru pomarańczowego oraz 20 ml soku z limonki z lodem przez 12 sekund.\n" +
                        "3. Przecedź do kieliszka.\n" +
                        "4. Udekoruj plasterkiem limonki.",
                imageResId = R.drawable.margarita,
                category = "drink"
            ),
            Cocktail(
                name = "PINA COLADA",
                ingredients = "Rum, mleko kokosowe, sok ananasowy, lód",
                instructions = "1. Do blendera wlej 50 ml białego rumu, 60 ml mleka kokosowego, 80 ml soku ananasowego i garść lodu.\n" +
                        "2. Miksuj na wysokich obrotach do uzyskania kremowej konsystencji.\n" +
                        "3. Przelej do kielicha typu hurricane.\n" +
                        "4. Udekoruj kawałkiem ananasa i wisienką koktajlową.",
                imageResId = R.drawable.pinacolada,
                category = "drink"
            ),
            Cocktail(
                name = "OLD FASHIONED",
                ingredients = "Bourbon, cukier trzcinowy, Angostura bitters, woda, lód, skórka pomarańczy",
                instructions = "1. W niskiej szklance umieść 1 kostkę cukru, 2-3 krople Angostura bitters i odrobinę wody.\n" +
                        "2. Rozgnieć całość muddlerem, aż cukier się rozpuści.\n" +
                        "3. Dodaj 60 ml bourbona i kilka dużych kostek lodu.\n" +
                        "4. Delikatnie zamieszaj.\n" +
                        "5. Udekoruj skręconą skórką pomarańczy.",
                imageResId = R.drawable.oldfashioned,
                category = "drink"
            ),
            Cocktail(
                name = "NEGRONI",
                ingredients = "Gin, Campari, słodki wermut, lód, plasterek pomarańczy",
                instructions = "1. Wymieszaj w szklance typu lowball: 30 ml ginu, 30 ml Campari, 30 ml słodkiego wermutu z lodem.\n" +
                        "2. Zamieszaj barmańską łyżką przez kilka sekund.\n" +
                        "3. Udekoruj plasterkiem pomarańczy.",
                imageResId = R.drawable.negroni,
                category = "drink"
            ),
            Cocktail(
                name = "WHISKEY SOUR",
                ingredients = "Whiskey, sok z cytryny, cukier, białko jajka (opcjonalnie), lód, wisienka koktajlowa",
                instructions = "1. W shakerze umieść 50 ml whiskey, 30 ml soku z cytryny, 15 ml syropu cukrowego i białko jajka.\n" +
                        "2. Wstrząsaj bez lodu suchy shake, następnie z lodem przez 10 sekund.\n" +
                        "3. Przecedź do kieliszka.\n" +
                        "4. Udekoruj wisienką koktajlową.",
                imageResId = R.drawable.whiskysour,
                category = "drink"
            ),
            Cocktail(
                name = "DAIQUIRI",
                ingredients = "Rum, sok z limonki, cukier trzcinowy, lód",
                instructions = "1. W shakerze umieść 50 ml białego rumu, 25 ml soku z limonki i 15 ml syropu cukrowego z lodem.\n" +
                        "2. Wstrząsaj przez 12 sekund.\n" +
                        "3. Przecedź do schłodzonego kieliszka.<nl>Udekoruj skórką z limonki.",
                imageResId = R.drawable.daiquiri,
                category = "drink"
            ),
            Cocktail(
                name = "MANHATTAN",
                ingredients = "Whiskey rye, słodki wermut, Angostura bitters, lód, skórka z wiśni maraschino",
                instructions = "1. Wymieszaj w szklance: 50 ml whiskey rye, 25 ml słodkiego wermutu, 2 krople Angostura z lodem.\n" +
                        "2. Zamieszaj do schłodzenia zawartości.\n" +
                        "3. Przecedź do kieliszka koktajlowego.\n" +
                        "4. Udekoruj wisienką maraschino.",
                imageResId = R.drawable.manhattan,
                category = "drink"
            ),
            Cocktail(
                name = "APEROL SPRITZ",
                ingredients = "Aperol, prosecco, woda gazowana, lód, plaster pomarańczy",
                instructions = "1. W kieliszku wypełnionym lodem połącz 60 ml Prosecco i 40 ml Aperolu.\n" +
                        "2. Dopełnij wodą gazowaną.\n" +
                        "3. Delikatnie zamieszaj.\n" +
                        "4. Udekoruj plasterkiem pomarańczy.",
                imageResId = R.drawable.aperolspritz,
                category = "drink"
            ),
            Cocktail(
                name = "LONG ISLAND ICED TEA",
                ingredients = "Wódka, gin, rum, tequila, triple sec, sok z cytryny, cola, lód, plaster cytryny",
                instructions = "1. W shakerze wymieszaj po 15 ml: wódki, ginu, białego rumu, tequili i triple sec z 25 ml soku z cytryny i lodem.\n" +
                        "2. Przecedź do wysokiej szklanki z lodem.\n" +
                        "3. Dopełnij colą.\n" +
                        "4. Udekoruj plasterkiem cytryny.",
                imageResId = R.drawable.longislandicedtea,
                category = "drink"
            ),
            Cocktail(
                name = "CAIPIRINHA",
                ingredients = "Cachaça, limonka, cukier trzcinowy, lód",
                instructions = "1. Pokrój limonkę na ćwiartki, usuń białą część.\n" +
                        "2. W szklance umieść limonkę i 2 łyżeczki cukru trzcinowego.\n" +
                        "3. Ugnieć muddlerem.\n" +
                        "4. Dodaj 50 ml cachaçy i lód.\n" +
                        "5. Delikatnie zamieszaj.",
                imageResId = R.drawable.caipirinha,
                category = "drink"
            ),
            Cocktail(
                name = "JAGER BOMB",
                ingredients = "Jagermeister, Red Bull, shot, szklanka",
                instructions = "1. Napełnij szklankę do połowy Red Bullem.\n" +
                        "2. Wlej 30 ml Jagermeistera do kieliszka shot.\n" +
                        "3. Upuść kieliszek do szklanki i wypij od razu.",
                imageResId = R.drawable.jager_bomb,
                category = "drink"
            ),

            // SHOTY
            Cocktail(
                name = "KAMIKAZE",
                ingredients = "Wódka, Blue Curaçao, sok z limonki, lód",
                instructions = "1. W shakerze umieść 25 ml wódki, 25 ml Blue Curaçao i 20 ml soku z limonki z lodem.\n" +
                        "2. Wstrząśnij przez 10 sekund.\n" +
                        "3. Przecedź do kieliszka shot.",
                imageResId = R.drawable.kamikaze,
                category = "shot"
            ),
            Cocktail(
                name = "B-52",
                ingredients = "Kahlua, Baileys Irish Cream, Grand Marnier",
                instructions = "1. Delikatnie warstwuj 20 ml Kahlua na dnie kieliszka shot.\n" +
                        "2. Nałóż 20 ml Baileys, przytrzymując łyżeczkę przy ściance.\n" +
                        "3. Na wierzchu warstwuj 20 ml Grand Marnier.",
                imageResId = R.drawable.b52,
                category = "shot"
            ),
            Cocktail(
                name = "WŚCIEKŁY PIES",
                ingredients = "Wódka, syrop malinowy, tabasco, lód",
                instructions = "1. Wlej 30 ml wódki do szklanki shot.\n" +
                        "2. Dodaj 15 ml syropu malinowego.\n" +
                        "3. Kilka kropli Tabasco na wierzch.\n" +
                        "4. Podawaj natychmiast.",
                imageResId = R.drawable.wsciekly_pies,
                category = "shot"
            ),
            Cocktail(
                name = "TEQUILA SHOT",
                ingredients = "Tequila, sól, limonka",
                instructions = "1. Nałóż szczyptę soli na dłoń między kciukiem a palcem wskazującym.\n" +
                        "2. Poliż sól, wypij 30 ml tequili jednym łykiem.\n" +
                        "3. Ugryź kawałek limonki.",
                imageResId = R.drawable.tequila_shot,
                category = "shot"
            ),
            Cocktail(
                name = "LEMON DROP",
                ingredients = "Wódka cytrynowa, sok z cytryny, cukier, lód, cukier na brzeg kieliszka",
                instructions = "1. Zwilż brzeg kieliszka sokiem z cytryny i obtocz w cukrze.\n" +
                        "2. W shakerze wstrząśnij 40 ml wódki cytrynowej, 20 ml soku z cytryny i 15 ml syropu cukrowego z lodem przez 10 sekund.\n" +
                        "3. Przecedź do kieliszka shot.",
                imageResId = R.drawable.lemon_drop,
                category = "shot"
            ),
            Cocktail(
                name = "BUTTERY NIPPLE",
                ingredients = "Butterscotch schnapps, Baileys Irish Cream",
                instructions = "1. Delikatnie wlej 20 ml Butterscotch schnapps do kieliszka shot.\n" +
                        "2. Następnie warstwuj 20 ml Baileys, powoli wlewając na łyżeczkę.",
                imageResId = R.drawable.buttery_nipple,
                category = "shot"
            ),
            Cocktail(
                name = "SAMBUCA",
                ingredients = "Sambuca, ziarna kawy (3 szt.)",
                instructions = "1. Napełnij kieliszek shot Sambucą.\n" +
                        "2. Dodaj 3 ziarna kawy do napoju.\n" +
                        "3. Podawaj schłodzone.",
                imageResId = R.drawable.sambuca,
                category = "shot"
            ),

            // DRINKI BEZALKOHOLOWE
            Cocktail(
                name = "VIRGIN MOJITO",
                ingredients = "Limonka, mięta, cukier trzcinowy, soda, lód",
                instructions = "1. Ugnieć ćwiartkę limonki z 8-10 liśćmi mięty i 2 łyżeczkami cukru w szklance.\n" +
                        "2. Dodaj kruszony lód do pełna.\n" +
                        "3. Dopełnij wodą gazowaną.\n" +
                        "4. Zamieszaj i udekoruj gałązką mięty.",
                imageResId = R.drawable.mojito,
                category = "soft"
            ),
            Cocktail(
                name = "SHIRLEY TEMPLE",
                ingredients = "Ginger ale, syrop grenadynowy, wisienka koktajlowa, lód",
                instructions = "1. Wlej 120 ml ginger ale do szklanki z lodem.\n" +
                        "2. Dodaj 15 ml syropu grenadynowego.\n" +
                        "3. Udekoruj wisienką koktajlową.",
                imageResId = R.drawable.shirley_temple,
                category = "soft"
            ),
            Cocktail(
                name = "VIRGIN COLADA",
                ingredients = "Sok ananasowy, mleko kokosowe, syrop cukrowy, lód",
                instructions = "1. W blenderze połącz 80 ml soku ananasowego, 40 ml mleka kokosowego, 15 ml syropu cukrowego i garść lodu.\n" +
                        "2. Miksuj do gładkości.\n" +
                        "3. Przelej do kielicha i udekoruj kawałkiem ananasa.",
                imageResId = R.drawable.pinacolada,
                category = "soft"
            ),
            Cocktail(
                name = "FRUIT PUNCH",
                ingredients = "Sok pomarańczowy, sok ananasowy, grenadyna, soda, lód",
                instructions = "1. Wymieszaj w shakerze 80 ml soku pomarańczowego, 80 ml soku ananasowego i 15 ml grenadyny z lodem.\n" +
                        "2. Przecedź do szklanki z lodem.\n" +
                        "3. Dopełnij sodą i delikatnie zamieszaj.",
                imageResId = R.drawable.fruit_punch,
                category = "soft"
            ),
            Cocktail(
                name = "CINDERELLA",
                ingredients = "Sok pomarańczowy, sok ananasowy, sok cytrynowy, grenadyna, lód",
                instructions = "1. W shakerze wymieszaj 60 ml soku pomarańczowego, 30 ml soku ananasowego, 15 ml soku cytrynowego i 10 ml grenadyny z lodem.\n" +
                        "2. Wstrząsaj przez 10 sekund.\n" +
                        "3. Przecedź do kieliszka.",
                imageResId = R.drawable.cinderella,
                category = "soft"
            ),
            Cocktail(
                name = "VIRGIN STRAWBERRY DAIQUIRI",
                ingredients = "Truskawki, sok z limonki, cukier, lód",
                instructions = "1. W blenderze miksuj 100 g truskawek, 30 ml soku z limonki, 15 g cukru i garść lodu.\n" +
                        "2. Miksuj do gładkiej konsystencji.\n" +
                        "3. Przelej do kieliszka.",
                imageResId = R.drawable.virgin_strawberry_daiquiri,
                category = "soft"
            ),
            Cocktail(
                name = "ARNOLD PALMER",
                ingredients = "Mrożona herbata, lemoniada, lód, plaster cytryny",
                instructions = "1. W szklance połącz pół na pół mrożoną herbatę i lemoniadę.\n" +
                        "2. Dodaj lód.\n" +
                        "3. Udekoruj plasterkiem cytryny.",
                imageResId = R.drawable.arnold_palmer,
                category = "soft"
            ),
            Cocktail(
                name = "BERRY FIZZ",
                ingredients = "Syrop malinowy, sok z cytryny, soda, świeże owoce, lód",
                instructions = "1. Wymieszaj 20 ml syropu malinowego z 20 ml soku z cytryny w szklance.\n" +
                        "2. Dodaj lód i uzupełnij sodą.\n" +
                        "3. Udekoruj świeżymi owocami.",
                imageResId = R.drawable.berry_fizz,
                category = "soft"
            ),
            Cocktail(
                name = "CUCUMBER COOLER",
                ingredients = "Ogórek, sok z limonki, mięta, syrop cukrowy, soda, lód",
                instructions = "1. Zblenduj plasterki ogórka z 20 ml soku z limonki i listkami mięty.\n" +
                        "2. Przecedź otrzymany płyn do szklanki z lodem.\n" +
                        "3. Dodaj 15 ml syropu cukrowego i dopełnij sodą.\n" +
                        "4. Delikatnie zamieszaj.",
                imageResId = R.drawable.cucumber_cooler,
                category = "soft"
            ),
            Cocktail(
                name = "TROPICAL SUNRISE",
                ingredients = "Sok pomarańczowy, sok z mango, grenadyna, lód",
                instructions = "1. Wlej 100 ml soku pomarańczowego i 50 ml soku z mango do wysokiej szklanki z lodem.\n" +
                        "2. Delikatnie wlej 10 ml grenadyny, aby opadła na dno.\n" +
                        "3. Podawaj bez mieszania, z widocznym gradientem.",
                imageResId = R.drawable.tropical_sunrise,
                category = "soft"
            )
        )
        dao.insertAll(sample)
    }

    suspend fun resetAndInsertData() {
        dao.deleteAll()
        insertInitialData()
    }
}
