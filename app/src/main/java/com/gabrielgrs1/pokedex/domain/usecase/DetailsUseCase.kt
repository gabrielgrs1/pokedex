package com.gabrielgrs1.pokedex.domain.usecase

import com.gabrielgrs1.pokedex.core.platform.UseCaseResult
import com.gabrielgrs1.pokedex.data.datasource.detail.PokemonDetailDao
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.data.model.toEntity
import com.gabrielgrs1.pokedex.domain.model.PokemonDetail
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class DetailsUseCase(
    private val detailsRepository: DetailsRepository,
    private val dao: PokemonDetailDao,
    private val dispatcher: CoroutineContext = Dispatchers.IO
) {
    suspend operator fun invoke(name: String): Flow<UseCaseResult<PokemonDetail>> = flow {
        try {
            val cachedPokemon = dao.getPokemonDetailByName(name)

            if (cachedPokemon != null) {
                val cachedPokemonsConverted = cachedPokemon.toDomain()

                emit(UseCaseResult.Success(cachedPokemonsConverted))
            } else {
                val pokemonDetail = detailsRepository.getPokemonDetailByName(name)

                dao.insertPokemonDetail(pokemonDetail.toEntity())

                emit(UseCaseResult.Success(pokemonDetail.toDomain()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(UseCaseResult.Error(e.message))
        }
    }.flowOn(dispatcher)
}