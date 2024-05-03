package com.gabrielgrs1.pokedex.core

import com.gabrielgrs1.pokedex.BuildConfig
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

const val NETWORK_TIMEOUT = 2L

val appModule = module {
    single {
        HttpLoggingInterceptor { message ->
            Timber.d("Http: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}

val searchPokemonsModule = module {
    // TODO
}

val pokemonDetailsModule = module {
    // TODO
}

val listPokemonsModule = module {
    // TODO
}

val favoritePokemonsModule = module {
    // TODO
}

