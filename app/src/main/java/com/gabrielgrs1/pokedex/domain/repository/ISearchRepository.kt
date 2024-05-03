package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.domain.model.Pokemon

interface ISearchRepository {

    suspend fun getPokemonByName(name: String): Pokemon?
}
