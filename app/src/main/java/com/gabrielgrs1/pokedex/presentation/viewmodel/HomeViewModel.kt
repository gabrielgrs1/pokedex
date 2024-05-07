package com.gabrielgrs1.pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.domain.usecase.ListUseCase
import com.gabrielgrs1.pokedex.presentation.uistate.HomeUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val listUseCase: ListUseCase,
    private val coroutineContext: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var page = 0

    init {
        listPokemons()
    }

    fun listPokemons() {
        viewModelScope.launch(coroutineContext) {
            listUseCase(page).collect {
                when (it) {
                    is UseCaseResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            content = emptyList(),
                            isLoading = false,
                            isError = true
                        )
                    }

                    is UseCaseResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            content = it.value,
                            isLoading = false,
                            isError = false
                        )
                    }
                }
            }
        }
    }
}