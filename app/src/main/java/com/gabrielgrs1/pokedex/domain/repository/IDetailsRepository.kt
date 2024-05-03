package com.gabrielgrs1.pokedex.domain.repository

import com.gabrielgrs1.pokedex.domain.model.PokemonDetail

interface IDetailsRepository {

    suspend fun getDetail(name: String): PokemonDetail
}
