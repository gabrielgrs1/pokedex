package com.gabrielgrs1.pokedex.data.datasource.detail

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabrielgrs1.pokedex.data.model.PokemonDetailEntity
import com.gabrielgrs1.pokedex.data.model.PokemonEntity

@Dao
interface PokemonDetailDao {
    // Details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(pokemonDetail: PokemonDetailEntity)

    @Query("SELECT * FROM pokemon_detail WHERE name = :name")
    suspend fun getPokemonDetailByName(name: String): PokemonDetailEntity?

    @Query("UPDATE pokemon_detail SET is_favorite = :favorite WHERE name = :name")
    suspend fun favoritePokemon(name: String, favorite: Boolean)
}