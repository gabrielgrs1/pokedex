package com.gabrielgrs1.pokedex.domain.usecase

import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.datasource.PokemonDao
import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.data.model.toEntity
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import com.gabrielgrs1.pokedex.domain.repository.ListRepository
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class ListUseCase(
    private val listRepository: ListRepository,
    private val dao: PokemonDao,
    private val dispatcher: CoroutineContext = Dispatchers.IO
) {
    suspend operator fun invoke(page: Int): Flow<UseCaseResult<List<Pokemon>>> = flow {
        try {
            val pokemonList = arrayListOf<Pokemon>()
            val cachedPokemons = dao.getPokemonListByPage(page)

            if (cachedPokemons.isNotEmpty()) {
                val cachedPokemonsConverted = cachedPokemons.map { it.toDomain() }
                emit(UseCaseResult.Success(cachedPokemonsConverted))
            } else {
                val pokemonListEntity = arrayListOf<PokemonEntity>()

                listRepository.listPokemons(page).results?.map {
                    pokemonListEntity.add(it.toEntity(page))
                    pokemonList.add(it.toDomain())
                }

                dao.insertPokemonPage(pokemonListEntity)

                emit(UseCaseResult.Success(pokemonList))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(UseCaseResult.Error(e.message))
        }
    }.flowOn(dispatcher)
}