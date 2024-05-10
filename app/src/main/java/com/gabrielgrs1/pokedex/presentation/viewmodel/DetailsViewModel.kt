package com.gabrielgrs1.pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.datasource.detail.PokemonDetailDao
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.usecase.DetailsUseCase
import com.gabrielgrs1.pokedex.presentation.uistate.DetailsUiState
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailsViewModel(
    private val detailsUseCase: DetailsUseCase,
    private val dao: PokemonDetailDao,
    private val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob(),
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailsUiState> =
        MutableStateFlow(DetailsUiState(isLoading = true))
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun getPokemon(name: String) {
        viewModelScope.launch(coroutineContext) {
            try {
                detailsUseCase(name).collect {
                    when (it) {
                        is UseCaseResult.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                pokemon = null,
                                isError = true,
                                errorMessage = ""
                            )
                        }

                        is UseCaseResult.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                pokemon = it.value,
                                isError = false,
                                errorMessage = ""
                            )
                        }
                    }
                }

            } catch (e: HttpException) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    pokemon = null,
                    isError = true,
                    errorMessage = e.message()
                )
            }
        }
    }

    fun favoritePokemon(name: String, isFavorite: Boolean) {
        viewModelScope.launch(coroutineContext) {
            try {
                dao.favoritePokemon(name, isFavorite)

                dao.getPokemonDetailByName(name)?.toDomain()?.let { pokemonDetail ->
                    pokemonDetail.isFavorite = isFavorite

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        pokemon = pokemonDetail,
                        isError = false,
                        errorMessage = ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}