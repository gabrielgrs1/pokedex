package com.gabrielgrs1.pokedex.usecase

import app.cash.turbine.test
import com.gabrielgrs1.pokedex.MainDispatcherRule
import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.datasource.PokemonDao
import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.data.model.PokemonResponse
import com.gabrielgrs1.pokedex.data.model.PokemonResult
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.repository.ListRepository
import com.gabrielgrs1.pokedex.domain.usecase.ListUseCase
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
class ListUsecaseTest {
    private val listRepository: ListRepository = mockk(relaxed = true)
    private val dao: PokemonDao = mockk(relaxed = true)
    private lateinit var listUseCase: ListUseCase

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        listUseCase = ListUseCase(
            listRepository = listRepository,
            dao = dao,
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `when list use case invoked given cached list is filled then emit success with domain object at entity`() =
        runTest {
            // Given
            val page = 1

            val pokemonList = listOf(
                PokemonEntity(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                    isFavorite = false
                )
            )
            val expectedValue = UseCaseResult.Success(pokemonList.map { it.toDomain() })
            coEvery { dao.getPokemonListByPage(page) } returns pokemonList


            // When
            listUseCase(page).test {
                // Then
                assertEquals(expectedValue, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `when list use case invoked given cached list is empty then emit success with domain object at reponse`() =
        runTest {
            // Given
            val page = 1

            val pokemonResponse = PokemonResponse(
                count = 0,
                next = "",
                previous = "",
                results = listOf(
                    PokemonResult(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/1/",
                    )
                )
            )

            val expectedValue =
                UseCaseResult.Success(pokemonResponse.results!!.map { it.toDomain() })

            coEvery { dao.getPokemonListByPage(page) } returns emptyList()
            coEvery { listRepository.listPokemons(page) } returns pokemonResponse


            // When
            listUseCase(page).test {
                // Then
                assertEquals(expectedValue, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `when list use case invoked given dao produce a exception then emit error`() =
        runTest {
            // Given
            val page = 1
            val message = "Error"
            val expectedValue =
                UseCaseResult.Error(message)

            coEvery { dao.getPokemonListByPage(page) } throws Exception(message)

            // When
            listUseCase(page).test {
                // Then
                assertEquals(expectedValue, awaitItem())
                awaitComplete()
            }
        }
}