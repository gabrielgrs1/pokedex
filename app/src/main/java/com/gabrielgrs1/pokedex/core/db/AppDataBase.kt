package com.gabrielgrs1.pokedex.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gabrielgrs1.pokedex.data.datasource.detail.PokemonDetailDao
import com.gabrielgrs1.pokedex.data.datasource.list.PokemonListDao
import com.gabrielgrs1.pokedex.data.model.PokemonDetailEntity
import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.domain.converters.ListStringConverter

@Database(entities = [(PokemonEntity::class), (PokemonDetailEntity::class)], version = 1)
@TypeConverters(ListStringConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun pokemonListDao(): PokemonListDao
    abstract fun pokemonDetailDao(): PokemonDetailDao
}