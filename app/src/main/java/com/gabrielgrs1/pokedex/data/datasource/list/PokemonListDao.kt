package com.gabrielgrs1.pokedex.data.datasource.list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabrielgrs1.pokedex.data.model.PokemonDetailEntity
import com.gabrielgrs1.pokedex.data.model.PokemonEntity

@Dao
interface PokemonListDao {
    // List
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonPage(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon WHERE page = :page")
    suspend fun getPokemonListByPage(page: Int): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE name = :name")
    suspend fun getPokemonByName(name: String): List<PokemonEntity>?
}