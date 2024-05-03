package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.data.datasource.CacheDao
import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.domain.repository.IInsertRepository

class InsertRepositoryImpl(
    private val cacheDao: CacheDao
) : IInsertRepository {
    override suspend fun insertPokemons(pokemons: List<PokemonEntity>) =
        cacheDao.insertPokemonPage(pokemons)
}
