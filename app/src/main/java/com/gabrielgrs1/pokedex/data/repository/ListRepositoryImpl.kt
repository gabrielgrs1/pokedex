package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.data.datasource.PokemonDao
import com.gabrielgrs1.pokedex.data.datasource.ListApi
import com.gabrielgrs1.pokedex.data.model.PokemonResponse
import com.gabrielgrs1.pokedex.domain.repository.ListRepository

class ListRepositoryImpl(
    private val api: ListApi,
    private val pokemonDao: PokemonDao
) : ListRepository {

    override suspend fun listPokemons(page: Int): PokemonResponse =
        api.listPokemons(limit = PAGE_LIMIT, offset = page * PAGE_LIMIT)


    companion object {
        const val PAGE_LIMIT = 20
    }
}
