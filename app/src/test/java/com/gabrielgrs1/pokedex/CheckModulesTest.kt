package com.gabrielgrs1.pokedex

import com.gabrielgrs1.pokedex.core.di.detailsModule
import com.gabrielgrs1.pokedex.core.di.homeModule
import com.gabrielgrs1.pokedex.core.di.networkConfigurationModule
import com.gabrielgrs1.pokedex.core.di.searchModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() {
        homeModule.verify()
        detailsModule.verify()
        searchModule.verify()
        networkConfigurationModule.verify()
    }
}