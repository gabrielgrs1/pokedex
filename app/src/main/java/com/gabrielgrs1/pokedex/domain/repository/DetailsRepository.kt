package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.data.model.PokemonDetailResponse

interface DetailsRepository {

    suspend fun getPokemonDetailByName(name: String): PokemonDetailResponse
}
