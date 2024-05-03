package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.data.datasource.CacheDao
import com.gabrielgrs1.pokedex.data.model.toPokemon
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import com.gabrielgrs1.pokedex.domain.repository.ISearchRepository

class SearchRespositoryImpl(private val dao: CacheDao) : ISearchRepository {

    override suspend fun getPokemonByName(name: String): Pokemon =
        dao.getPokemonByName(name).toPokemon()
}
