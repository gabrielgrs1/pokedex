package com.gabrielgrs1.pokedex.data.model

import android.os.Parcelable
import com.gabrielgrs1.pokedex.core.Constants.IMAGE_URL
import com.gabrielgrs1.pokedex.core.getPokemonIndex
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

fun PokemonResult.toDomain(): Pokemon {
    val index = this.url?.getPokemonIndex()

    return Pokemon(
        name = this.name.orEmpty(),
        imageUrl = if (index.isNullOrEmpty().not()) "$IMAGE_URL$index.png"
        else ""
    )
}

fun PokemonResult.toEntity(page: Int) = PokemonEntity(
    name = this.name.orEmpty(),
    page = page,
    url = this.url.orEmpty()
)
