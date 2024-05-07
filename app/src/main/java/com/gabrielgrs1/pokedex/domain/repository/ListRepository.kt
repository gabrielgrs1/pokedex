package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.data.model.PokemonResponse

interface ListRepository {

    suspend fun listPokemons(page: Int): PokemonResponse
}
