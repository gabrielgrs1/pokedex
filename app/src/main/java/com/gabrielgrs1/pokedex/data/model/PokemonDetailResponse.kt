package com.gabrielgrs1.pokedex.data.model

import android.os.Parcelable
import com.gabrielgrs1.pokedex.domain.model.PokemonDetail
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetailResponse(
    val id: Int? = null,
    val name: String? = null,
    val weight: Int? = null,
    val height: String? = null,
    val types: List<Types>? = null,
    val stats: List<Stats>? = null,
    val abilities: List<Abilities>? = null,
    val sprites: Sprites? = null,
) : Parcelable {

    internal fun getImageUrl() = sprites?.other?.officialArtwork?.frontDefault.orEmpty()
    internal fun getStats() = stats?.map { it.stat?.name.orEmpty() }.orEmpty()
    internal fun getTypes() = types?.map { it.type?.name.orEmpty() }.orEmpty()
    internal fun getAbilities() =
        abilities?.map { it.ability?.name?.replace('-', ' ').orEmpty() }.orEmpty()

    @Parcelize
    data class Sprites(
        val other: Other? = null
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
        val stat: Stat? = null
    ) : Parcelable {

        @Parcelize
        data class Stat(
            val name: String? = null
        ) : Parcelable
    }

    @Parcelize
    data class Types(
        val type: Type? = null,
    ) : Parcelable {

        @Parcelize
        data class Type(
            val name: String? = null
        ) : Parcelable
    }

    @Parcelize
    data class Abilities(
        val ability: Ability? = null,
    ) : Parcelable {

        @Parcelize
        data class Ability(
            val name: String? = null
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