package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.domain.model.PokemonDetail

interface DetailsRepository {

    suspend fun getDetail(name: String): PokemonDetail

    // TODO
//    suspend fun favorite(pokemon: PokemonDetailEntity)
}
