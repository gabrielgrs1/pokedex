package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.core.platform.Result
import com.gabrielgrs1.pokedex.data.datasource.detail.DetailsApi
import com.gabrielgrs1.pokedex.data.datasource.detail.PokemonDetailDao
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.data.model.toEntity
import com.gabrielgrs1.pokedex.domain.model.PokemonDetail
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DetailsRepositoryImpl(
    private val api: DetailsApi,
    private val dao: PokemonDetailDao,
) : DetailsRepository {
    override suspend fun getPokemonDetailByName(name: String): Flow<Result<PokemonDetail>> = flow {
        try {
            val cachedPokemon = dao.getPokemonDetailByName(name)

            if (cachedPokemon != null) {
                val cachedPokemonsConverted = cachedPokemon.toDomain()

                emit(Result.Success(cachedPokemonsConverted))
            } else {
                val pokemonDetail = api.getDetail(name)

                dao.insertPokemonDetail(pokemonDetail.toEntity())

                emit(Result.Success(pokemonDetail.toDomain()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message))
        }
    }

    override suspend fun favoritePokemon(
        name: String,
        isFavorite: Boolean
    ): Flow<Result<PokemonDetail>> =
        flow {
            try {
                dao.favoritePokemon(name, isFavorite)

                dao.getPokemonDetailByName(name)?.let {
                    emit(Result.Success(it.toDomain()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message))
            }
        }
}
