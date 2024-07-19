package com.gabrielgrs1.pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielgrs1.pokedex.core.platform.Result
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository
import com.gabrielgrs1.pokedex.presentation.uistate.DetailsUiState
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val detailsRepository: DetailsRepository,
    private val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob(),
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailsUiState> =
        MutableStateFlow(DetailsUiState(isLoading = true))
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun getPokemon(name: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
        )

        viewModelScope.launch(coroutineContext) {
            detailsRepository.getPokemonDetailByName(name).collect {
                when (it) {
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            pokemon = null,
                            isError = true,
                            errorMessage = it.messageError.orEmpty()
                        )
                    }

                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            pokemon = it.value,
                            isError = false,
                            errorMessage = ""
                        )
                    }

                    Result.Empty -> { /* Do Nothing */
                    }
                }
            }
        }
    }

    fun favoritePokemon(name: String, isFavorite: Boolean) {
        viewModelScope.launch(coroutineContext) {
            detailsRepository.favoritePokemon(name, isFavorite).collect {
                when (it) {
                    is Result.Error -> { /* Do Nothing */
                    }

                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            pokemon = it.value,
                            isError = false,
                            errorMessage = ""
                        )
                    }

                    Result.Empty -> { /* Do Nothing */
                    }
                }
            }
        }
    }
}