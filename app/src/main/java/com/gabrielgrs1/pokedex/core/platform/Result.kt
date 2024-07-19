package com.gabrielgrs1.pokedex.core.platform

sealed interface Result<out T> {
    data class Success<T>(val value: T) : Result<T>
    data class Error(val messageError: String? = null) : Result<Nothing>
    data object Empty : Result<Nothing>
}
