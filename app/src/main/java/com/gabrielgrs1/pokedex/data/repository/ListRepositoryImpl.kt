package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.core.platform.Result
import com.gabrielgrs1.pokedex.data.datasource.list.ListApi
import com.gabrielgrs1.pokedex.data.datasource.list.PokemonListDao
import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.data.model.toEntity
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import com.gabrielgrs1.pokedex.domain.repository.ListRepository
import java.net.HttpURLConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class ListRepositoryImpl(
    private val api: ListApi,
    private val dao: PokemonListDao,
) : ListRepository {

    override suspend fun listPokemons(page: Int): Flow<Result<List<Pokemon>>> = flow {
        try {
            val pokemonList = arrayListOf<Pokemon>()
            val cachedPokemons = dao.getPokemonListByPage(page)

            if (cachedPokemons.isNotEmpty()) {
                val cachedPokemonsConverted = cachedPokemons.map { it.toDomain() }
                emit(Result.Success(cachedPokemonsConverted))
            } else {
                val pokemonListEntity = arrayListOf<PokemonEntity>()

                api.listPokemons(limit = PAGE_LIMIT, offset = page * PAGE_LIMIT).results?.map {
                    pokemonListEntity.add(it.toEntity(page))
                    pokemonList.add(it.toDomain())
                }

                dao.insertPokemonPage(pokemonListEntity)

                emit(Result.Success(pokemonList))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message))
        }
    }


    override suspend fun searchPokemon(name: String): Flow<Result<Pokemon>> = flow {
        try {
            val pokemon = api.searchPokemon(name).toDomain()
            emit(Result.Success(pokemon))
        } catch (httpException: HttpException) {
            if (httpException.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                emit(Result.Empty)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message))
        }
    }

    companion object {
        const val PAGE_LIMIT = 20
    }
}
