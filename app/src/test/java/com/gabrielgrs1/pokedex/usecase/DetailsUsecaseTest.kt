package com.gabrielgrs1.pokedex.usecase

import app.cash.turbine.test
import com.gabrielgrs1.pokedex.MainDispatcherRule
import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.datasource.detail.PokemonDetailDao
import com.gabrielgrs1.pokedex.data.model.PokemonDetailEntity
import com.gabrielgrs1.pokedex.data.model.PokemonDetailResponse
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository
import com.gabrielgrs1.pokedex.domain.usecase.DetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsUsecaseTest {
    private val detailsRepository: DetailsRepository = mockk(relaxed = true)
    private val dao: PokemonDetailDao = mockk(relaxed = true)
    private lateinit var detailsUseCase: DetailsUseCase

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        detailsUseCase = DetailsUseCase(
            detailsRepository = detailsRepository,
            dao = dao,
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `when list use case invoked given cached pokemon is filled then emit success with domain object at entity`() =
        runTest {
            // Given
            val pokemonName = "bulbasaur"
            val pokemonDetailEntity = PokemonDetailEntity(
                id = 1,
                name = pokemonName,
                weight = "1",
                height = "1",
                types = listOf(""),
                stats = listOf(""),
                abilities = listOf(""),
                imageUrl = "",
                isFavorite = false
            )
            val expectedValue = UseCaseResult.Success(pokemonDetailEntity.toDomain())

            coEvery { dao.getPokemonDetailByName(pokemonName) } returns pokemonDetailEntity


            // When
            detailsUseCase(pokemonName).test {
                // Then
                assertEquals(expectedValue, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `when list use case invoked given cached list is empty then emit success with domain object at reponse`() =
        runTest {
            // Given
            val pokemonName = "bulbasaur"
            val pokemonDetail = PokemonDetailResponse(
                id = 1,
                name = pokemonName,
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

            val expectedValue =
                UseCaseResult.Success(pokemonDetail.toDomain())

            coEvery { dao.getPokemonDetailByName(pokemonName) } returns null
            coEvery { detailsRepository.getPokemonDetailByName(pokemonName) } returns pokemonDetail


            // When
            detailsUseCase(pokemonName).test {
                // Then
                assertEquals(expectedValue, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `when list use case invoked given dao produce a exception then emit error`() =
        runTest {
            // Given
            val pokemonName = "bulbasaur"
            val message = "Error"
            val expectedValue =
                UseCaseResult.Error(message)

            coEvery { dao.getPokemonDetailByName(pokemonName) } throws Exception(message)

            // When
            detailsUseCase(pokemonName).test {
                // Then
                assertEquals(expectedValue, awaitItem())
                awaitComplete()
            }
        }
}