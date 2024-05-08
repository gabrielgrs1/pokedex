package com.gabrielgrs1.pokedex.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gabrielgrs1.pokedex.R
import com.gabrielgrs1.pokedex.presentation.components.PokemonListItem
import com.gabrielgrs1.pokedex.presentation.uistate.HomeUiState
import com.gabrielgrs1.pokedex.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    modifier: Modifier = Modifier,
    onEndOfListReached: () -> Unit = {},
    onPokemonClicked: (String) -> Unit = {}
) {
    val currentProgress by remember {
        mutableFloatStateOf(0f)
    }

    when {
        homeUiState.pokemonList.isEmpty().not() -> {
            val pokemonList = homeUiState.pokemonList
            val scrollState = rememberLazyGridState()

            LazyVerticalGrid(
                columns = GridCells.Fixed(count = GRID_SIZE),
                state = scrollState,
                modifier = modifier.fillMaxSize()
            ) {
                items(pokemonList) { pokemon ->
                    Box(Modifier.clickable(onClick = { onPokemonClicked(pokemon.name) })) {
                        PokemonListItem(pokemon)
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

        homeUiState.isLoading -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    progress = { currentProgress },
                    modifier = modifier.fillMaxSize(),
                )
            }
        }

        homeUiState.isError -> {
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.generic_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }
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
    HomeScreen(
        homeUiState = uiState,
        modifier = modifier,
        onEndOfListReached = homeViewModel::getNextPage,
        onPokemonClicked = onPokemonClicked
    )
}

const val GRID_SIZE = 2