package com.gabrielgrs1.pokedex.data.model

import android.os.Parcelable
import com.gabrielgrs1.pokedex.core.formatPokemonName
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonSearchResponse(
    @SerializedName("name") val name: String? = null,
    @SerializedName("sprites") val sprites: Sprites? = null,
) : Parcelable {

    internal fun getImageUrl() = sprites?.other?.officialArtwork?.frontDefault.orEmpty()

    @Parcelize
    data class Sprites(
        @SerializedName("other") val other: Other? = null
    ) : Parcelable {

        @Parcelize
        data class Other(
            @SerializedName("official-artwork") val officialArtwork: OfficialArtwork? = null
        ) : Parcelable {

            @Parcelize
            data class OfficialArtwork(
                @SerializedName("front_default") val frontDefault: String? = null
            ) : Parcelable
        }
    }
}


fun PokemonSearchResponse.toDomain() = Pokemon(
    name = name.orEmpty().formatPokemonName(),
    imageUrl = getImageUrl(),
)