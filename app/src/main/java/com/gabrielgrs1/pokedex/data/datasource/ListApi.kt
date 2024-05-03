package com.gabrielgrs1.pokedex.data.datasource

import com.gabrielgrs1.pokedex.data.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ListApi {

    @GET("/pokemon")
    suspend fun listPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): List<PokemonResponse>

}