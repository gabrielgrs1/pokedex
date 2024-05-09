package com.gabrielgrs1.pokedex.core

import com.gabrielgrs1.pokedex.core.Constants.POKEMON_ID_INDEX
import java.util.Locale

fun String.getPokemonIndex() = this.split("/")[POKEMON_ID_INDEX]

fun String.formatToUserFriendly() = this
    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    .replace("-", " ")

fun String.unformatPokemonName() = this
    .replaceFirstChar { if (it.isUpperCase()) it.lowercase(Locale.getDefault()) else it.toString() }
    .replace(" ", "-")