package com.gabrielgrs1.pokedex.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabrielgrs1.pokedex.presentation.components.EmptySearch
import com.gabrielgrs1.pokedex.presentation.components.Error
import com.gabrielgrs1.pokedex.presentation.components.Loading
import com.gabrielgrs1.pokedex.presentation.components.PokemonListItem
import com.gabrielgrs1.pokedex.presentation.uistate.HomeUiState
import com.gabrielgrs1.pokedex.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onEndOfListReached: () -> Unit = {},
    onPokemonClicked: (String) -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    loadingProgress: Float,
    searchText: String,
) {
    Box(
        modifier = modifier
            .padding(top = 16.dp)
    ) {

        // Search text
        TextField(
            value = searchText,
            onValueChange = { newValue ->
                onSearchTextChange(newValue)
            },
            label = { Text("Search Pokemon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )

        when {
            uiState.pokemonList.isEmpty().not() -> {
                ContentState(
                    uiState = uiState,
                    modifier = modifier.padding(top = 8.dp),
                    onPokemonClicked = onPokemonClicked,
                    onEndOfListReached = onEndOfListReached,
                    isNotSearch = searchText.isEmpty()
                )
            }

            uiState.isLoading -> Loading(currentProgressLoading = loadingProgress)
            uiState.isError -> Error()
            uiState.isEmpty -> EmptySearch()
        }
    }
}

@Composable
private fun ContentState(
    uiState: HomeUiState,
    modifier: Modifier,
    onPokemonClicked: (String) -> Unit,
    onEndOfListReached: () -> Unit,
    isNotSearch: Boolean,
) {
    val pokemonList = uiState.pokemonList
    val scrollState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = GRID_SIZE),
        state = scrollState,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        items(pokemonList) { pokemon ->
            Box(
                modifier = Modifier.clickable(onClick = { onPokemonClicked(pokemon.name) })
            ) {
                PokemonListItem(pokemon)
            }
        }
    }

    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd(isNotSearch)
        }
    }

    LaunchedEffect(endOfListReached) { onEndOfListReached() }
}

fun LazyGridState.isScrolledToEnd(isNotSearch: Boolean) =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1 && isNotSearch


@Composable
fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel(),
    onPokemonClicked: (String) -> Unit,
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val searchQuery by homeViewModel.searchQuery.collectAsState()

    val currentProgress by remember {
        mutableFloatStateOf(2f)
    } // TODO: Maybe this should be in the viewmodel

    HomeScreen(
        uiState = uiState,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        onEndOfListReached = homeViewModel::getNextPage,
        onPokemonClicked = onPokemonClicked,
        onSearchTextChange = homeViewModel::searchPokemon,
        loadingProgress = currentProgress,
        searchText = searchQuery
    )
}

const val GRID_SIZE = 2