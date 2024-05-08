package com.gabrielgrs1.pokedex.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gabrielgrs1.pokedex.core.NavigationItem
import com.gabrielgrs1.pokedex.presentation.screen.DetailsScreenRoute
import com.gabrielgrs1.pokedex.presentation.screen.HomeScreenRoute

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreenRoute(modifier = Modifier, onPokemonClicked = { pokemonName ->
                navController.navigate("${NavigationItem.Details.route}/$pokemonName")
            })
        }
        composable(
            "${NavigationItem.Details.route}/{pokemonName}",
            arguments = listOf(navArgument("pokemonName") {
                type = NavType.StringType
            })
        ) {
            val pokemonName = it.arguments?.getString("pokemonName")
            DetailsScreenRoute(modifier = Modifier, pokemonName = pokemonName)
        }
    }
}