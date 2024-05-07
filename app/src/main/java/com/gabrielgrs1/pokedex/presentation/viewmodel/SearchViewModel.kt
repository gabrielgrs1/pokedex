package com.gabrielgrs1.pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielgrs1.pokedex.data.datasource.PokemonDao
import com.gabrielgrs1.pokedex.data.model.toPokemon
import com.gabrielgrs1.pokedex.presentation.uistate.SearchUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val dao: PokemonDao,
    private val coroutineContext: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState(isLoading = false))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun searchPokemon(name: String) {
        viewModelScope.launch(coroutineContext) {
            try {
                _uiState.value = SearchUiState(isLoading = true)
                val result = dao.getPokemonByName(name)

                if (result.isNullOrEmpty().not()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        content = result?.map { it.toPokemon() } ?: emptyList(),
                        isError = false,
                        isEmpty = false
                    )
                } else if (result.isNullOrEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        content = null,
                        isError = false,
                        isEmpty = true
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    content = null,
                    isError = true,
                    isEmpty = false
                )
            }
        }
    }
}