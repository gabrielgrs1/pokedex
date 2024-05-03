package com.gabrielgrs1.pokedex.domain.usecase

import com.gabrielgrs1.pokedex.data.model.PokemonEntity
import com.gabrielgrs1.pokedex.data.model.toPokemon
import com.gabrielgrs1.pokedex.data.model.toPokemonEntity
import com.gabrielgrs1.pokedex.data.repository.InsertRepositoryImpl
import com.gabrielgrs1.pokedex.data.repository.ListRepositoryImpl
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class ListPokemonsUseCase(
    private val listRepository: ListRepositoryImpl,
    private val insertRepository: InsertRepositoryImpl,
) {
    operator fun invoke(page: Int): Flow<List<Pokemon>> = flow {
        val pokemonList = arrayListOf<Pokemon>()
        val cachedPokemons = listRepository.listPokemonsCache(page)

        if (cachedPokemons.isNotEmpty()) {
            emit(cachedPokemons.map { it.toPokemon() })
        }

        val pokemonListEntity = arrayListOf<PokemonEntity>()

        listRepository.listPokemonsApi(page).map {
            pokemonListEntity.add(it.toPokemonEntity(page))
            pokemonList.add(it.toPokemon())
        }

        insertRepository.insertPokemons(pokemonListEntity)

        emit(pokemonList)
    }
}