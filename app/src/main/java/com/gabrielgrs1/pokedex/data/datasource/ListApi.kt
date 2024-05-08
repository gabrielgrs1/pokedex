package com.gabrielgrs1.pokedex.data.datasource

import com.gabrielgrs1.pokedex.core.Constants
import com.gabrielgrs1.pokedex.data.model.PokemonDetailResponse
import com.gabrielgrs1.pokedex.data.model.PokemonResponse
import com.gabrielgrs1.pokedex.data.model.PokemonSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ListApi {

    @GET(Constants.API.INFIX_URL + Constants.API.ROUTE_LIST)
    suspend fun listPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): PokemonResponse


    @GET("${Constants.API.INFIX_URL}${Constants.API.ROUTE_LIST}{name}")
    suspend fun searchPokemon(
        @Path("name") name: String
    ): PokemonSearchResponse
}