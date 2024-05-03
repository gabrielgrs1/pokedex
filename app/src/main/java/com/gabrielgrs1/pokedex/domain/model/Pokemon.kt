package com.gabrielgrs1.pokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val name: String,
    val imageUrl: String
) : Parcelable