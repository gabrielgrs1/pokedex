package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.data.model.PokemonResponse

interface IListRepository {

    suspend fun listPokemonsApi(page: Int): List<PokemonResponse>
    suspend fun listPokemonsCache(page: Int): List<PokemonEntity>
}
