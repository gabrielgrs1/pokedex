package com.gabrielgrs1.pokedex.data.model

import android.os.Parcelable
import com.gabrielgrs1.pokedex.domain.model.PokemonDetail
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetailResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("weight") val weight: Int? = null,
    @SerializedName("height") val height: String? = null,
    @SerializedName("types") val types: List<Types>? = null,
    @SerializedName("stats") val stats: List<Stats>? = null,
    @SerializedName("abilities") val abilities: List<Abilities>? = null,
    @SerializedName("sprites") val sprites: Sprites? = null,
) : Parcelable {

    internal fun getImageUrl() = sprites?.other?.officialArtwork?.frontDefault.orEmpty()
    internal fun getStats() = stats?.map { it.stat?.name.orEmpty() }.orEmpty()
    internal fun getTypes() = types?.map { it.type?.name.orEmpty() }.orEmpty()
    internal fun getAbilities() =
        abilities?.map { it.ability?.name?.replace('-', ' ').orEmpty() }.orEmpty()

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

    @Parcelize
    data class Stats(
        @SerializedName("base_stat") val baseStat: Int? = null,
        @SerializedName("stat") val stat: Stat? = null
    ) : Parcelable {

        @Parcelize
        data class Stat(
            @SerializedName("name") val name: String? = null
        ) : Parcelable
    }

    @Parcelize
    data class Types(
        @SerializedName("type") val type: Type? = null,
    ) : Parcelable {

        @Parcelize
        data class Type(
            @SerializedName("name") val name: String? = null
        ) : Parcelable
    }

    @Parcelize
    data class Abilities(
        @SerializedName("ability") val ability: Ability? = null,
    ) : Parcelable {

        @Parcelize
        data class Ability(
            @SerializedName("name") val name: String? = null
        ) : Parcelable
    }
}


const val INVALID_ID = -1

fun PokemonDetailResponse.toDomain() = PokemonDetail(
    id = id ?: INVALID_ID,
    name = name.orEmpty(),
    imageUrl = getImageUrl(),
    stats = getStats(),
    types = getTypes(),
    abilities = getAbilities(),
    weight = weight.toString(),
    height = height.orEmpty()
)