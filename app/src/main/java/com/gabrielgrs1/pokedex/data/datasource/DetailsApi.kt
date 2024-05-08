package com.gabrielgrs1.pokedex.data.datasource

import com.gabrielgrs1.pokedex.core.Constants
import com.gabrielgrs1.pokedex.data.model.PokemonDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsApi {

    @GET("${Constants.API.INFIX_URL}${Constants.API.ROUTE_LIST}{name}")
    suspend fun getDetail(
        @Path("name") name: String
    ): PokemonDetailResponse

}