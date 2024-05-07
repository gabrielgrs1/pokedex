package com.gabrielgrs1.pokedex.data.model

import android.os.Parcelable
import com.gabrielgrs1.pokedex.BuildConfig
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonResponse(
    @SerializedName("count") val count: Int? = null,
    @SerializedName("next") val next: String? = null,
    @SerializedName("previous") val previous: String? = null,
    @SerializedName("results") val results: List<PokemonResult>? = null
) : Parcelable

@Parcelize
data class PokemonResult(
    @SerializedName("name") val name: String? = null,
    @SerializedName("url") val url: String? = null
) : Parcelable

fun PokemonResult.toPokemon(): Pokemon {
    val index = this.url?.split("/")?.last()

    return Pokemon(
        name = this.name.orEmpty(),
        imageUrl = if (index.isNullOrEmpty().not()) BuildConfig.IMAGE_URL + index + ".png"
        else ""
    )
}

fun PokemonResult.toPokemonEntity(page: Int) = PokemonEntity(
    name = this.name.orEmpty(),
    page = page,
    url = this.url.orEmpty()
)
