package com.gabrielgrs1.pokedex.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gabrielgrs1.pokedex.data.datasource.PokemonDao
import com.gabrielgrs1.pokedex.data.model.PokemonEntity

@Database(entities = [(PokemonEntity::class)], version = 2)
abstract class AppDataBase : RoomDatabase() {
    abstract fun pokemonDao() : PokemonDao
}