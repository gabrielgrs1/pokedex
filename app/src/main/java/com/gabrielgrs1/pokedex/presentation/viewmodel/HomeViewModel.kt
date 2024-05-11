package com.gabrielgrs1.pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.repository.ListRepository
import com.gabrielgrs1.pokedex.domain.usecase.ListUseCase
import com.gabrielgrs1.pokedex.presentation.uistate.HomeUiState
import java.net.HttpURLConnection
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import retrofit2.HttpException

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val listUseCase: ListUseCase,
    private val listRepository: ListRepository,
    private val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob(),
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var page = INITIAL_PAGE
    private var searchJob: Job? = null
    private var isSearchListing =
        false // This variable is used as a temporary fix to avoid the auto increment page when start app

    init {
        listPokemons()
        listenerSearchText()
    }

    private fun listenerSearchText() {
        viewModelScope.launch {
            searchQuery
                .debounce(DELAY_BETWEEN_SEARCHES)
                .collectLatest { query ->
                    if (query.length >= MIN_POKEMON_NAME_SIZE) {
                        isSearchListing = true
                        searchPokemon(query)
                    } else if (isSearchListing && query.isEmpty()) {
                        isSearchListing = false
                        page = INITIAL_PAGE
                        _uiState.value = HomeUiState()
                        listPokemons()
                    }
                }
        }
    }

    fun listPokemons() {
        viewModelScope.launch(coroutineContext) {
            listUseCase(page).collect {
                when (it) {
                    is UseCaseResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            pokemonList = emptyList(),
                            isLoading = false,
                            isError = true,
                            isEmpty = false,
                            errorMessage = ""
                        )
                    }

                    is UseCaseResult.Success -> {
                        val newList = _uiState.value.pokemonList.toMutableList() + it.value

                        _uiState.value = _uiState.value.copy(
                            pokemonList = newList,
                            isLoading = false,
                            isError = false,
                            isEmpty = false,
                            errorMessage = ""
                        )
                    }
                }
            }
        }
    }

    fun searchPokemon(name: String) {
        _searchQuery.value = name

        searchJob?.cancel() // Cancel the previous search job if it's still running
        searchJob = viewModelScope.launch(coroutineContext) {
            try {
                val result = listRepository.searchPokemon(name)

                _uiState.value = _uiState.value.copy(
                    pokemonList = listOf(result.toDomain()),
                    isError = false,
                    isLoading = false,
                    isEmpty = false,
                    errorMessage = ""
                )
            } catch (e: HttpException) {
                e.printStackTrace()
                if (e.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    _uiState.value = _uiState.value.copy(
                        pokemonList = emptyList(),
                        isError = false,
                        isLoading = false,
                        isEmpty = true,
                        errorMessage = e.message()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        pokemonList = emptyList(),
                        isError = true,
                        isLoading = false,
                        isEmpty = false,
                        errorMessage = e.message()
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
        const val INITIAL_PAGE = 0
        private const val DELAY_BETWEEN_SEARCHES = 500L
        private const val MIN_POKEMON_NAME_SIZE = 3
    }
}