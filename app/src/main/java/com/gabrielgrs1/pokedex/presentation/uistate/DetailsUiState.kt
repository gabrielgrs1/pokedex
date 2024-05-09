package com.gabrielgrs1.pokedex.presentation.uistate

import com.gabrielgrs1.pokedex.domain.model.PokemonDetail

data class DetailsUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val pokemon: PokemonDetail? = null,
)