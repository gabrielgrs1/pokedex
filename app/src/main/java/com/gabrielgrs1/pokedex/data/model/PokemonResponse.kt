package com.gabrielgrs1.pokedex.data.model

import android.os.Parcelable
import com.gabrielgrs1.pokedex.BuildConfig
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonResponse(
    val name: String? = null,
    val url: String? = null
) : Parcelable

fun PokemonResponse.toPokemon(): Pokemon {
    val index = this.url?.split("/")?.last()

    return Pokemon(
        name = this.name.orEmpty(),
        imageUrl = if (index.isNullOrEmpty().not()) BuildConfig.IMAGE_URL + index + ".png"
        else ""
    )
}

fun PokemonResponse.toPokemonEntity(page: Int) = PokemonEntity(
    name = this.name.orEmpty(),
    page = page,
    url = this.url.orEmpty()
)
