package com.gabrielgrs1.pokedex

import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.model.PokemonSearchResponse
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import com.gabrielgrs1.pokedex.domain.repository.ListRepository
import com.gabrielgrs1.pokedex.domain.usecase.ListUseCase
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
    private val listUseCase: ListUseCase = mockk(relaxed = true)
    private val listRepository: ListRepository = mockk(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var stateValue: HomeUiState

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        homeViewModel = HomeViewModel(
            listUseCase = listUseCase,
            listRepository = listRepository,
            coroutineContext = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `when init viewmodel loading is true`() = runTest {
        stateValue = homeViewModel.uiState.value
        assertEquals(true, stateValue.isLoading)
    }

    @Test
    fun `when listPokemons is called, it updates uiState correctly`() = runTest {
        val pokemonList = listOf(Pokemon("Pikachu", ""), Pokemon("Charmander", ""))

        coEvery { listUseCase(0) } returns flowOf(UseCaseResult.Success(pokemonList))

        homeViewModel.listPokemons()

        stateValue = homeViewModel.uiState.value
        assertEquals(pokemonList, stateValue.pokemonList)
        assertEquals(false, stateValue.isError)
        assertEquals(false, stateValue.isLoading)
        assertEquals(false, stateValue.isEmpty)
    }

    @Test
    fun `when listPokemons fails, it updates uiState correctly`() = runTest {
        coEvery { listUseCase(any()) } returns flowOf(UseCaseResult.Error("error"))
        stateValue = homeViewModel.uiState.value

        homeViewModel.listPokemons()

        stateValue = homeViewModel.uiState.value
        assertEquals(false, stateValue.isEmpty)
        assertEquals(true, stateValue.isError)
        assertEquals(false, stateValue.isLoading)
        assertEquals(true, stateValue.pokemonList.isEmpty())
    }

    @Test
    fun `when searchPokemon is called, it updates uiState correctly`() = runTest {
        val pokemon = PokemonSearchResponse(
            "Pikachu",
            PokemonSearchResponse.Sprites(
                PokemonSearchResponse.Sprites.Other(
                    PokemonSearchResponse.Sprites.Other.OfficialArtwork("")
                )
            )
        )

        coEvery { listRepository.searchPokemon(any()) } returns pokemon

        homeViewModel.searchPokemon("Pikachu")

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
    fun `when searchPokemon not found, it updates uiState correctly`() = runTest {
        coEvery { listRepository.searchPokemon(any()) } throws HttpException(
            Response.error<Any>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(null, "")
            )
        )

        homeViewModel.searchPokemon("Pikachu")

        stateValue = homeViewModel.uiState.value
        assertEquals(true, stateValue.isEmpty)
        assertEquals(false, stateValue.isError)
        assertEquals(false, stateValue.isLoading)
        assertEquals(true, stateValue.pokemonList.isEmpty())
    }

    @Test
    fun `when searchPokemon fails, it updates uiState correctly`() = runTest {
        coEvery { listRepository.searchPokemon(any()) } throws HttpException(
            Response.error<Any>(
                HttpURLConnection.HTTP_INTERNAL_ERROR,
                ResponseBody.create(null, "")
            )
        )

        homeViewModel.searchPokemon("Pikachu")

        stateValue = homeViewModel.uiState.value
        assertEquals(false, stateValue.isEmpty)
        assertEquals(true, stateValue.isError)
        assertEquals(false, stateValue.isLoading)
        assertEquals(true, stateValue.pokemonList.isEmpty())
    }

    @Test
    fun `when nextPage called then invoke listPokemons and update page`() = runTest {
        homeViewModel.getNextPage()

        var page = INITIAL_PAGE

        coVerify {
            listUseCase(page++)
        }
    }
}