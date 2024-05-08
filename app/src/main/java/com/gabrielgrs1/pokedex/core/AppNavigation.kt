package com.gabrielgrs1.pokedex.core

enum class Screen {
    HOME,
    DETAILS,
}
sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object Details : NavigationItem(Screen.DETAILS.name)
}