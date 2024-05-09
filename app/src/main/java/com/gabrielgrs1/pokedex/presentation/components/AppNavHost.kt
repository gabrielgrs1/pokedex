package com.gabrielgrs1.pokedex.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gabrielgrs1.pokedex.core.utils.Arguments
import com.gabrielgrs1.pokedex.core.utils.NavigationItem
import com.gabrielgrs1.pokedex.core.di.detailsModule
import com.gabrielgrs1.pokedex.core.di.homeModule
import com.gabrielgrs1.pokedex.core.utils.unformatPokemonName
import com.gabrielgrs1.pokedex.presentation.screen.DetailsScreenRoute
import com.gabrielgrs1.pokedex.presentation.screen.HomeScreenRoute
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

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
            loadKoinModules(homeModule)

            HomeScreenRoute(modifier = Modifier, onPokemonClicked = { pokemonName ->
                navController.navigate("${NavigationItem.Details.route}/${pokemonName}")
            })
        }
        composable(
            "${NavigationItem.Details.route}/{${Arguments.POKEMON_NAME}}",
            arguments = listOf(navArgument(Arguments.POKEMON_NAME) {
                type = NavType.StringType
            })
        ) {
            val pokemonName = it.arguments?.getString(Arguments.POKEMON_NAME)?.unformatPokemonName()

            unloadKoinModules(homeModule)
            loadKoinModules(detailsModule)

            DetailsScreenRoute(
                modifier = Modifier,
                pokemonName = pokemonName.orEmpty(),
                onBackPressed = {
                    navController.popBackStack()
                    unloadKoinModules(detailsModule)
                }
            )
        }
    }
}