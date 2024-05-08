package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.data.model.PokemonResponse
import com.gabrielgrs1.pokedex.data.model.PokemonSearchResponse

interface ListRepository {

    suspend fun listPokemons(page: Int): PokemonResponse

    suspend fun searchPokemon(name: String): PokemonSearchResponse
}
