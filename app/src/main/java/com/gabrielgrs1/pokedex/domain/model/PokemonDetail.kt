package com.gabrielgrs1.pokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetail(
    val id: Int,
    val name: String,
    val weight: String,
    val height: String,
    val types: List<String>,
    val stats: List<String>,
    val abilities: List<String>,
    val imageUrl: String,
    var isFavorite: Boolean = false
) : Parcelable