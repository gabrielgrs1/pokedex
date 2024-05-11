package com.gabrielgrs1.pokedex.core

import android.app.Application
import com.gabrielgrs1.pokedex.core.di.dataBaseModule
import com.gabrielgrs1.pokedex.core.di.homeModule
import com.gabrielgrs1.pokedex.core.di.networkConfigurationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PokedexApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@PokedexApplication)

            modules(
                networkConfigurationModule,
                dataBaseModule,
                homeModule
            )
        }
    }
}