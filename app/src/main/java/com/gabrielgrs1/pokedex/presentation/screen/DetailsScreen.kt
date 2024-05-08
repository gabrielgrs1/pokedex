package com.gabrielgrs1.pokedex.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.gabrielgrs1.pokedex.presentation.uistate.DetailsUiState
import com.gabrielgrs1.pokedex.presentation.viewmodel.DetailsViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun DetailsScreen(uiState: DetailsUiState, modifier: Modifier = Modifier) {

}

@Composable
fun DetailsScreenRoute(
    modifier: Modifier = Modifier,
    pokemonName: String,
    detailsViewModel: DetailsViewModel = koinViewModel()
) {
    val uiState by detailsViewModel.uiState.collectAsState()

    DetailsScreen(
        uiState = uiState,
        modifier = modifier
    )
}