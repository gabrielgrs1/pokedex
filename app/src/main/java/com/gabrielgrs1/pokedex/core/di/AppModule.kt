package com.gabrielgrs1.pokedex.core.di

import androidx.room.Room
import com.gabrielgrs1.pokedex.BuildConfig
import com.gabrielgrs1.pokedex.core.db.AppDataBase
import com.gabrielgrs1.pokedex.core.utils.Constants
import com.gabrielgrs1.pokedex.data.datasource.DetailsApi
import com.gabrielgrs1.pokedex.data.datasource.ListApi
import com.gabrielgrs1.pokedex.data.datasource.PokemonDao
import com.gabrielgrs1.pokedex.data.repository.DetailsRepositoryImpl
import com.gabrielgrs1.pokedex.data.repository.ListRepositoryImpl
import com.gabrielgrs1.pokedex.domain.repository.DetailsRepository
import com.gabrielgrs1.pokedex.domain.repository.ListRepository
import com.gabrielgrs1.pokedex.domain.usecase.ListUseCase
import com.gabrielgrs1.pokedex.presentation.viewmodel.DetailsViewModel
import com.gabrielgrs1.pokedex.presentation.viewmodel.HomeViewModel
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val NETWORK_TIMEOUT = 30L

val networkConfigurationModule = module {
    single {
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

val detailsModule = module {
    single { get<Retrofit>().create(DetailsApi::class.java) }
    single<DetailsRepository> { DetailsRepositoryImpl(get()) }

    viewModel { DetailsViewModel(detailsRepository = get(), dao = get()) }
}

val homeModule = module {
    single { get<Retrofit>().create(ListApi::class.java) }

    single<ListRepository> { ListRepositoryImpl(api = get()) }


    factory {
        ListUseCase(
            listRepository = get(),
            dao = get(),

            )
    }

    viewModel {
        HomeViewModel(
            listUseCase = get(),
            listRepository = get()
        )
    }
}

fun provideDao(postDataBase: AppDataBase): PokemonDao = postDataBase.pokemonDao()

val dataBaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDataBase::class.java, Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
    single { provideDao(get()) }
}
