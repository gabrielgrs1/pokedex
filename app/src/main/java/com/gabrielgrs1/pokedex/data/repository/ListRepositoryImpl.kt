package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.data.datasource.ListApi
import com.gabrielgrs1.pokedex.data.datasource.CacheDao
import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.data.model.PokemonResponse
import com.gabrielgrs1.pokedex.domain.repository.IListRepository

class ListRepositoryImpl(
    private val api: ListApi,
    private val cacheDao: CacheDao
) : IListRepository {
    override suspend fun listPokemonsCache(page: Int): List<PokemonEntity> =
        cacheDao.getPokemonListByPage(page)

    override suspend fun listPokemonsApi(page: Int): List<PokemonResponse> =
        api.listPokemons(limit = PAGE_LIMIT, offset = page * PAGE_LIMIT)

    companion object {
        const val PAGE_LIMIT = 50
    }
}
