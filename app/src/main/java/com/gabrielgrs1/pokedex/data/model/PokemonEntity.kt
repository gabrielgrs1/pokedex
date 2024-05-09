package com.gabrielgrs1.pokedex.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gabrielgrs1.pokedex.core.utils.Constants.IMAGE_URL
import com.gabrielgrs1.pokedex.core.utils.getPokemonIndex
import com.gabrielgrs1.pokedex.domain.model.Pokemon

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val name: String,
    val url: String,
    var page: Int = 0,
    var isFavorite: Boolean = false,
)

fun PokemonEntity?.toDomain() = Pokemon(
    name = this?.name.orEmpty(),
    imageUrl = if (this == null) "" else "$IMAGE_URL${this.url.getPokemonIndex()}.png"
)
