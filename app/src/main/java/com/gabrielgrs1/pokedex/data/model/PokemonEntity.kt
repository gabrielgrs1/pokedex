package com.gabrielgrs1.pokedex.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gabrielgrs1.pokedex.BuildConfig
import com.gabrielgrs1.pokedex.domain.model.Pokemon

@Entity
data class PokemonEntity(
    @PrimaryKey val name: String,
    val url: String,
    var page: Int = 0,
)

fun PokemonEntity?.toPokemon() = Pokemon(
    name = this?.name.orEmpty(),
    imageUrl = if (this == null) "" else BuildConfig.IMAGE_URL + this.url.split("/").last() + ".png"
)
