package com.gabrielgrs1.pokedex.core

import android.app.Application
import com.gabrielgrs1.pokedex.BuildConfig
import com.gabrielgrs1.pokedex.core.di.dataBaseModule
import com.gabrielgrs1.pokedex.core.di.homeModule
import com.gabrielgrs1.pokedex.core.di.networkConfigurationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class PokedexApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@PokedexApplication)

            modules(
                networkConfigurationModule,
                homeModule,
                dataBaseModule
            )
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}