package com.gabrielgrs1.pokedex.viewmodel

import com.gabrielgrs1.pokedex.MainDispatcherRule
import com.gabrielgrs1.pokedex.core.platform.Result
import com.gabrielgrs1.pokedex.data.model.PokemonSearchResponse
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import com.gabrielgrs1.pokedex.domain.repository.ListRepository
import com.gabrielgrs1.pokedex.presentation.uistate.HomeUiState
import com.gabrielgrs1.pokedex.presentation.viewmodel.HomeViewModel
import com.gabrielgrs1.pokedex.presentation.viewmodel.HomeViewModel.Companion.INITIAL_PAGE
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.net.HttpURLConnection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val listRepository: ListRepository = mockk(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var stateValue: HomeUiState

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        homeViewModel = HomeViewModel(
            listRepository = listRepository,
            coroutineContext = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `when init viewmodel then loading is true`() = runTest {
        // Given
        stateValue = homeViewModel.uiState.value

        // Then
        assertEquals(true, stateValue.isLoading)
    }

    @Test
    fun `when listPokemons is called given listUseCase returns success then update uiState with pokemon list`() =
        runTest {
            // Given
            val pokemonList = listOf(Pokemon("Pikachu", ""), Pokemon("Charmander", ""))
            coEvery { listRepository.listPokemons(0) } returns flowOf(Result.Success(pokemonList))

            // When
            homeViewModel.listPokemons()

            // Then
            stateValue = homeViewModel.uiState.value
            assertEquals(pokemonList, stateValue.pokemonList)
            assertEquals(false, stateValue.isError)
            assertEquals(false, stateValue.isLoading)
            assertEquals(false, stateValue.isEmpty)
        }

    @Test
    fun `when listPokemons fails given listUseCase returns error then update uiState with error`() =
        runTest {
            // Given
            coEvery { listRepository.listPokemons(any()) } returns flowOf(Result.Error("error"))

            // When
            homeViewModel.listPokemons()

            // Then
            stateValue = homeViewModel.uiState.value
            assertEquals(false, stateValue.isEmpty)
            assertEquals(true, stateValue.isError)
            assertEquals(false, stateValue.isLoading)
            assertEquals(true, stateValue.pokemonList.isEmpty())
        }

    @Test
    fun `when searchPokemon is called given repository returns success then update uiState with search success`() =
        runTest {
            // Given
            val pokemon = PokemonSearchResponse(
                "Pikachu",
                PokemonSearchResponse.Sprites(
                    PokemonSearchResponse.Sprites.Other(
                        PokemonSearchResponse.Sprites.Other.OfficialArtwork("")
                    )
                )
            )

            coEvery { listRepository.searchPokemon(any()) } returns flowOf(Result.Success(pokemon.toDomain()))

            // When
            homeViewModel.searchPokemon("Pikachu")

            // Then
            stateValue = homeViewModel.uiState.value
            assertEquals(
                listOf(pokemon.toDomain()),
                stateValue.pokemonList
            )
            assertEquals(false, stateValue.isLoading)
            assertEquals(false, stateValue.isError)
            assertEquals(false, stateValue.isEmpty)
        }

    @Test
    fun `when searchPokemon given returns not found then update uiState with empty state`() =
        runTest {
            // Given
            coEvery { listRepository.searchPokemon(any()) } throws HttpException(
                Response.error<Any>(
                    HttpURLConnection.HTTP_NOT_FOUND,
                    ResponseBody.create(null, "")
                )
            )

            // When
            homeViewModel.searchPokemon("Pikachu")

            // Then
            stateValue = homeViewModel.uiState.value
            assertEquals(true, stateValue.isEmpty)
            assertEquals(false, stateValue.isError)
            assertEquals(false, stateValue.isLoading)
            assertEquals(true, stateValue.pokemonList.isEmpty())
        }

    @Test
    fun `when searchPokemon was called given returns fails then updates uiState with error`() =
        runTest {
            // Given
            coEvery { listRepository.searchPokemon(any()) } throws HttpException(
                Response.error<Any>(
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    ResponseBody.create(null, "")
                )
            )

            // When
            homeViewModel.searchPokemon("Pikachu")

            // Then
            stateValue = homeViewModel.uiState.value
            assertEquals(false, stateValue.isEmpty)
            assertEquals(true, stateValue.isError)
            assertEquals(false, stateValue.isLoading)
            assertEquals(true, stateValue.pokemonList.isEmpty())
        }

    @Test
    fun `when nextPage called then invoke listPokemons and update page`() = runTest {
        // Given
        var page = INITIAL_PAGE

        // When
        homeViewModel.getNextPage()

        // Then
        coVerify {
            listRepository.listPokemons(page++)
        }
    }
}