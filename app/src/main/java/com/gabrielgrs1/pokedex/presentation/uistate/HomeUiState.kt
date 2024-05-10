package com.gabrielgrs1.pokedex.presentation.uistate

import com.gabrielgrs1.pokedex.domain.model.Pokemon

data class HomeUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isEmpty: Boolean = false,
    val errorMessage: String = "",
    val pokemonList: List<Pokemon> = emptyList(),
)