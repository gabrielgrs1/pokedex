package com.gabrielgrs1.pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.repository.ListRepository
import com.gabrielgrs1.pokedex.domain.usecase.ListUseCase
import com.gabrielgrs1.pokedex.presentation.uistate.HomeUiState
import java.net.HttpURLConnection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val listUseCase: ListUseCase,
    private val listRepository: ListRepository,
    private val coroutineContext: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var page = INITIAL_PAGE

    init {
        listPokemons()
    }

    private fun listPokemons() {
        viewModelScope.launch(coroutineContext) {
            listUseCase(page).collect {
                when (it) {
                    is UseCaseResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            pokemonList = emptyList(),
                            isLoading = false,
                            isError = true,
                            isEmpty = false
                        )
                    }

                    is UseCaseResult.Success -> {
                        val newList = _uiState.value.pokemonList.toMutableList() + it.value

                        _uiState.value = _uiState.value.copy(
                            pokemonList = newList,
                            isLoading = false,
                            isError = false,
                            isEmpty = false
                        )
                    }
                }
            }
        }
    }

    fun searchPokemon(name: String) {
        viewModelScope.launch(coroutineContext) {
            try {
                _uiState.value = HomeUiState(isLoading = true)
                val result = listRepository.searchPokemon(name)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    pokemonList = listOf(result.toDomain()),
                    isError = false,
                    isEmpty = false
                )
            } catch (e: HttpException) {
                e.printStackTrace()
                if (e.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        pokemonList = emptyList(),
                        isError = false,
                        isEmpty = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        pokemonList = emptyList(),
                        isError = true,
                        isEmpty = false
                    )
                }
            }
        }
    }

    fun getNextPage() {
        page++
        listPokemons()
    }

    companion object {
        private const val INITIAL_PAGE = 0
    }
}