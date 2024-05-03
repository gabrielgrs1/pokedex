package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.data.model.PokemonResponse

interface IInsertRepository {

    suspend fun insertPokemons(pokemons: List<PokemonEntity>)
}
