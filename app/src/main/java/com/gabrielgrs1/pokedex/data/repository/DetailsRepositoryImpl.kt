package com.gabrielgrs1.pokedex.data.repository

import com.gabrielgrs1.pokedex.data.datasource.DetailsApi
import com.gabrielgrs1.pokedex.data.model.toDomain
import com.gabrielgrs1.pokedex.domain.model.PokemonDetail
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository

class DetailsRepositoryImpl(private val api: DetailsApi) : DetailsRepository {
    override suspend fun getDetail(name: String): PokemonDetail =
        api.getDetail(name).toDomain()
}
