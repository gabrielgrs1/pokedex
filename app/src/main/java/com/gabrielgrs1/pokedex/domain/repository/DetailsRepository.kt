package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow
import com.gabrielgrs1.pokedex.core.platform.Result

interface DetailsRepository {

    suspend fun getPokemonDetailByName(name: String): Flow<Result<PokemonDetail>>

    suspend fun favoritePokemon(name: String, isFavorite: Boolean): Flow<Result<PokemonDetail>>
}
