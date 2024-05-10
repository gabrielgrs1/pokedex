package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.data.datasource.list.ListApi
import com.gabrielgrs1.pokedex.data.model.PokemonResponse
import com.gabrielgrs1.pokedex.data.model.PokemonSearchResponse
import com.gabrielgrs1.pokedex.domain.repository.ListRepository

class ListRepositoryImpl(
    private val api: ListApi
) : ListRepository {

    override suspend fun listPokemons(page: Int): PokemonResponse =
        api.listPokemons(limit = PAGE_LIMIT, offset = page * PAGE_LIMIT)

    override suspend fun searchPokemon(name: String): PokemonSearchResponse =
        api.searchPokemon(name)

    companion object {
        const val PAGE_LIMIT = 20
    }
}
