package com.gabrielgrs1.pokedex.viewmodel

import com.gabrielgrs1.pokedex.MainDispatcherRule
import com.gabrielgrs1.pokedex.data.datasource.PokemonDao
import com.gabrielgrs1.pokedex.domain.model.PokemonDetail
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository
import com.gabrielgrs1.pokedex.presentation.uistate.DetailsUiState
import com.gabrielgrs1.pokedex.presentation.viewmodel.DetailsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.net.HttpURLConnection
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class DetailsViewModelTest {
    private val detailsRepository: DetailsRepository = mockk(relaxed = true)
    private val dao: PokemonDao = mockk(relaxed = true)
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var stateValue: DetailsUiState

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        detailsViewModel = DetailsViewModel(
            detailsRepository = detailsRepository,
            dao = dao,
            coroutineContext = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `when init viewmodel loading is true`() = runTest {
        stateValue = detailsViewModel.uiState.value
        assertEquals(true, stateValue.isLoading)
    }

    @Test
    fun `when getPokemon given repository return success then update ui state with success state`() =
        runTest {
            // Given
            val pokemonDetail = PokemonDetail(
                1,
                "",
                "1kg",
                "1M",
                listOf(""),
                listOf(""),
                listOf(""),
                "",
            )

            coEvery { detailsRepository.getDetail("pikachu") } returns pokemonDetail

            // When
            detailsViewModel.getPokemon("pikachu")

            // Then
            stateValue = detailsViewModel.uiState.value
            assertEquals(false, stateValue.isLoading)
            assertEquals(false, stateValue.isError)
            assertEquals(pokemonDetail, stateValue.pokemon)
        }


    @Test
    fun `when getPokemon was called given returns fails then updates uiState with error`() =
        runTest {
            // Given
            coEvery { detailsRepository.getDetail(any()) } throws HttpException(
                Response.error<Any>(
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    ResponseBody.create(null, "")
                )
            )

            // When
            detailsViewModel.getPokemon("Pikachu")

            // Then
            stateValue = detailsViewModel.uiState.value
            assertEquals(true, stateValue.isError)
            assertEquals(false, stateValue.isLoading)
            assertEquals(true, stateValue.pokemon == null)
        }

    @Test
    fun `when favoritePokemon was called given success then do not throw exception`() = runTest {
        // When
        detailsViewModel.favoritePokemon("Pikachu", true)

        // Then
        coVerify { dao.favoritePokemon("Pikachu", true) }

        // TODO: Test if the pokemon isFavorite is true at uiState
    }

}