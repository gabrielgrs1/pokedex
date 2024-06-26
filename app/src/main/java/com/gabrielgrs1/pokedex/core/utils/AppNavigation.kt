package com.gabrielgrs1.pokedex.core.utils

enum class Screen {
    HOME,
    DETAILS,
}

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object Details : NavigationItem(Screen.DETAILS.name)
}

object Arguments {
    const val POKEMON_NAME = "pokemonName"
}