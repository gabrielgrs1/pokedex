package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.core.platform.Result
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface ListRepository {

    suspend fun listPokemons(page: Int): Flow<Result<List<Pokemon>>>

    suspend fun searchPokemon(name: String): Flow<Result<Pokemon>>
}
