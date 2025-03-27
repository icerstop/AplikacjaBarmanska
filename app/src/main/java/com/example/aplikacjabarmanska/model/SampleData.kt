package com.example.aplikacjabarmanska.model

fun sampleCocktails(): List<Cocktail> = listOf(
    Cocktail(
        id = 1,
        name = "MOJITO",
        ingredients = "Rum, limonka, mięta, cukier, woda gazowana",
        instructions = "Rozgnieć limonkę z cukrem i miętą, dodaj rum i wodę gazowaną, podaj z lodem."
    ),
    Cocktail(
        id = 2,
        name = "COSMOPOLITAN",
        ingredients = "Wódka, Cointreau, sok żurawinowy, limonka",
        instructions = "Wstrząśnij wszystko w shakerze z lodem i podaj w kieliszku koktajlowym."
    ),
    Cocktail(
        id = 3,
        name = "MARGARITA",
        ingredients = "Tequila, likier pomarańczowy, sok z limonki",
        instructions = "Wstrząśnij składniki z lodem w shakerze i podaj w szkle z obręczką z soli."
    ),
    Cocktail(
        id = 4,
        name = "PINA COLADA",
        ingredients = "Rum, mleko kokosowe, sok ananasowy",
        instructions = "Wstrząśnij składniki z lodem w shakerze i podaj w szkle z kawałkiem ananasa."
    )
)
