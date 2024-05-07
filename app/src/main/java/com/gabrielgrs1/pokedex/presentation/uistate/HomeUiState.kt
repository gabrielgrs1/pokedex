package com.gabrielgrs1.pokedex.presentation.uistate

import com.gabrielgrs1.pokedex.domain.model.Pokemon

data class HomeUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val content: List<Pokemon> = listOf()
)