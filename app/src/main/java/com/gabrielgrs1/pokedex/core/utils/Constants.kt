package com.gabrielgrs1.pokedex.core.utils

object Constants {
    const val POKEMON_ID_INDEX = 6
    const val IMAGE_URL =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

    const val DATABASE_NAME = "pokedex_database"
    const val INVALID_ID = -1

    object ROUTES {
        const val INFIX_URL = "api/v2/"

        const val ROUTE_LIST = "pokemon/"
        const val ROUTE_DETAILS = "pokemon/{name}"
    }
}