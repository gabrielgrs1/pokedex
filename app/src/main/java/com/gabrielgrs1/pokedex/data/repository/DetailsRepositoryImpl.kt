package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.data.datasource.detail.DetailsApi
import com.gabrielgrs1.pokedex.data.model.PokemonDetailResponse
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository

class DetailsRepositoryImpl(private val api: DetailsApi) : DetailsRepository {
    override suspend fun getPokemonDetailByName(name: String): PokemonDetailResponse =
        api.getDetail(name)
}
