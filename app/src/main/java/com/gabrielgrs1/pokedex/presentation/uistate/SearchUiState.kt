package com.gabrielgrs1.pokedex.presentation.uistate

import com.gabrielgrs1.pokedex.domain.model.Pokemon

data class SearchUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isEmpty: Boolean = false,
    val content: List<Pokemon>? = listOf()
)