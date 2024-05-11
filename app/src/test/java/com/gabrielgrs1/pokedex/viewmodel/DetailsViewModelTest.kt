package com.gabrielgrs1.pokedex.viewmodel

import com.gabrielgrs1.pokedex.MainDispatcherRule
import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.datasource.detail.PokemonDetailDao
import com.gabrielgrs1.pokedex.data.model.PokemonDetailEntity
import com.gabrielgrs1.pokedex.data.model.PokemonDetailResponse
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.usecase.DetailsUseCase
import com.gabrielgrs1.pokedex.presentation.uistate.DetailsUiState
import com.gabrielgrs1.pokedex.presentation.viewmodel.DetailsViewModel
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
class DetailsViewModelTest {
    private val detailsUseCase: DetailsUseCase = mockk(relaxed = true)
    private val dao: PokemonDetailDao = mockk(relaxed = true)
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var stateValue: DetailsUiState

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        detailsViewModel = DetailsViewModel(
            detailsUseCase = detailsUseCase,
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
            val pokemonDetail = PokemonDetailResponse(
                id = 1,
                name = "",
                weight = 1,
                height = "1",
                types = listOf(
                    PokemonDetailResponse.Types(
                        PokemonDetailResponse.Types.Type("")
                    )
                ),
                stats = listOf(
                    PokemonDetailResponse.Stats(
                        1,
                        PokemonDetailResponse.Stats.Stat("")
                    )
                ),
                abilities = listOf(
                    PokemonDetailResponse.Abilities(
                        PokemonDetailResponse.Abilities.Ability(
                            ""
                        )
                    )
                ),
                sprites = PokemonDetailResponse.Sprites(
                    PokemonDetailResponse.Sprites.Other(
                        PokemonDetailResponse.Sprites.Other.OfficialArtwork("")
                    )
                ),
            )

            coEvery { detailsUseCase(any()) } returns flowOf(UseCaseResult.Success(pokemonDetail.toDomain()))

            // When
            detailsViewModel.getPokemon("pikachu")

            // Then
            stateValue = detailsViewModel.uiState.value
            assertEquals(false, stateValue.isLoading)
            assertEquals(false, stateValue.isError)
            assertEquals(pokemonDetail.toDomain(), stateValue.pokemon)
        }

    @Test
    fun `when detailsUseCase was called given returns fails then updates uiState with error`() =
        runTest {
            // Given
            val messageError = "error"
            coEvery { detailsUseCase(any()) } returns flowOf(UseCaseResult.Error(messageError))


            // When
            detailsViewModel.getPokemon("Pikachu")

            // Then
            stateValue = detailsViewModel.uiState.value
            assertEquals(true, stateValue.isError)
            assertEquals(false, stateValue.isLoading)
            assertEquals(true, stateValue.errorMessage == messageError)
            assertEquals(true, stateValue.pokemon == null)
        }

    @Test
    fun `when detailsUseCase was called given returns not found then updates uiState with not found`() =
        runTest {
            // Given
            val messageError = "error"
            val httpException = HttpException(
                Response.error<Any>(
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    ResponseBody.create(null, messageError)
                )
            )

            coEvery { detailsUseCase(any()) } throws httpException

            // When
            detailsViewModel.getPokemon("Pikachu")

            // Then
            stateValue = detailsViewModel.uiState.value
            assertEquals(true, stateValue.isError)
            assertEquals(false, stateValue.isLoading)
            assertEquals(true, stateValue.errorMessage == httpException.message())
            assertEquals(true, stateValue.pokemon == null)
        }

    @Test
    fun `when favoritePokemon was called given success then do not throw exception and update UI with pokemon favoited`() = runTest {
        // When
        val pokemonName = "pikachu"
        val isFavorite = true
        val pokemonDetailEntity = PokemonDetailEntity(
            id = 1,
            name = "",
            weight = "1",
            height = "1",
            types = listOf(""),
            stats = listOf(""),
            abilities = listOf(""),
            imageUrl = "",
            isFavorite = isFavorite
        )

        coEvery { dao.getPokemonDetailByName(pokemonName) } returns pokemonDetailEntity

        // Given
        detailsViewModel.favoritePokemon(pokemonName, isFavorite)

        // Then
        coVerify { dao.favoritePokemon(pokemonName, isFavorite) }

        stateValue = detailsViewModel.uiState.value
        assertEquals(false, stateValue.isError)
        assertEquals(false, stateValue.isLoading)
        assertEquals(true, stateValue.errorMessage.isEmpty())
        assertEquals(true, stateValue.pokemon == pokemonDetailEntity.toDomain())
    }

}