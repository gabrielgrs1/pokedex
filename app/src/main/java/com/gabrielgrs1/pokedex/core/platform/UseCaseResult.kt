package com.gabrielgrs1.pokedex.core.platform

sealed interface UseCaseResult<out T> {
    data class Success<T>(val value: T) : UseCaseResult<T>
    data class Error(val messageError: String? = null) : UseCaseResult<Nothing>
}
