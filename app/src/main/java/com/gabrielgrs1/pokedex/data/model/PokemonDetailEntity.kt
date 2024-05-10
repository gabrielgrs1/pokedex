package com.gabrielgrs1.pokedex.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gabrielgrs1.pokedex.domain.model.PokemonDetail

@Entity(tableName = "pokemon_detail")
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val stats: List<String>,
    val types: List<String>,
    val abilities: List<String>,
    val weight: String,
    val height: String,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
)

fun PokemonDetailEntity.toDomain() = PokemonDetail(
    id = id,
    name = name,
    imageUrl = imageUrl,
    stats = stats,
    types = types,
    abilities = abilities,
    weight = weight,
    height = height,
    isFavorite = isFavorite
)
