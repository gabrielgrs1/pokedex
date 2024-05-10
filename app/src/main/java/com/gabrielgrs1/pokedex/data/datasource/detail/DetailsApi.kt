package com.gabrielgrs1.pokedex.data.datasource.detail

import com.gabrielgrs1.pokedex.core.utils.Constants
import com.gabrielgrs1.pokedex.data.model.PokemonDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsApi {

    @GET("${Constants.ROUTES.INFIX_URL}${Constants.ROUTES.ROUTE_DETAILS}")
    suspend fun getDetail(
        @Path("name") name: String
    ): PokemonDetailResponse

}