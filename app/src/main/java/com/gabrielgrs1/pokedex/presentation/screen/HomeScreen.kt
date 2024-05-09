package com.gabrielgrs1.pokedex.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gabrielgrs1.pokedex.presentation.components.Error
import com.gabrielgrs1.pokedex.presentation.components.Loading
import com.gabrielgrs1.pokedex.presentation.components.PokemonListItem
import com.gabrielgrs1.pokedex.presentation.uistate.HomeUiState
import com.gabrielgrs1.pokedex.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    modifier: Modifier = Modifier,
    onEndOfListReached: () -> Unit = {},
    onPokemonClicked: (String) -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    loadingProgress: Float,
) {/* TODO: Implement search bar */

    when {
        homeUiState.pokemonList.isEmpty().not() -> {
            ContentState(homeUiState, modifier, onPokemonClicked, onEndOfListReached)
        }

        homeUiState.isLoading -> Loading(currentProgressLoading = loadingProgress)
        homeUiState.isError -> Error()
        homeUiState.isEmpty -> Error()
    }
}

@Composable
private fun ContentState(
    homeUiState: HomeUiState,
    modifier: Modifier,
    onPokemonClicked: (String) -> Unit,
    onEndOfListReached: () -> Unit
) {
    val pokemonList = homeUiState.pokemonList
    val scrollState = rememberLazyGridState()


    Surface(color = Color.LightGray) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = GRID_SIZE),
            state = scrollState,
            modifier = modifier
                .fillMaxSize()
                .padding(top = 48.dp)
        ) {
            items(pokemonList) { pokemon ->
                Box(
                    modifier = Modifier.clickable(onClick = { onPokemonClicked(pokemon.name) })
                ) {
                    PokemonListItem(pokemon)
                }
            }
        }
    }

    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }

    LaunchedEffect(endOfListReached) { onEndOfListReached() }
}

fun LazyGridState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1


@Composable
fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel(),
    onPokemonClicked: (String) -> Unit,
) {
    val uiState by homeViewModel.uiState.collectAsState()

    val currentProgress by remember {
        mutableFloatStateOf(2f)
    } // TODO: Maybe this should be in the viewmodel

    HomeScreen(
        homeUiState = uiState,
        modifier = modifier,
        onEndOfListReached = homeViewModel::getNextPage,
        onPokemonClicked = onPokemonClicked,
        onSearchTextChange = homeViewModel::searchPokemon,
        loadingProgress = currentProgress
    )
}

const val GRID_SIZE = 2