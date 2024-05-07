package com.gabrielgrs1.pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielgrs1.pokedex.data.datasource.PokemonDao
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository
import com.gabrielgrs1.pokedex.presentation.uistate.DetailsUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailsViewModel(
    private val detailsRepository: DetailsRepository,
    private val dao: PokemonDao,
    private val coroutineContext: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailsUiState> =
        MutableStateFlow(DetailsUiState(isLoading = true))
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun getPokemon(name: String) {
        viewModelScope.launch(coroutineContext) {
            try {
                val pokemonDetail = detailsRepository.getDetail(name)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    content = pokemonDetail,
                    isError = false
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    content = null,
                    isError = true
                )
            }
        }
    }

    fun favoritePokemon(name: String, isFavorite: Boolean) {
        viewModelScope.launch(coroutineContext) {
            try {
                dao.favoritePokemon(name, isFavorite)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}