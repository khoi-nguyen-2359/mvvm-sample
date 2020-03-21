package com.media2359.genflix.dagger

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.media2359.genflix.MainApp
import com.media2359.shared.dagger.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import khoina.weatherforecast.ForecastListViewModel
import khoina.weatherforecast.dagger.ViewModelKey
import khoina.weatherforecast.data.retrofit.ForecastApi
import khoina.weatherforecast.data.room.ForecastDao
import khoina.weatherforecast.data.room.ForecastDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module(includes = [AppModule.Bindings::class])
class AppModule {

    @Module
    interface Bindings {
        @Binds
        fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

        @Binds
        @IntoMap
        @ViewModelKey(ForecastListViewModel::class)
        fun forecastListViewModel(forecastListViewModel: ForecastListViewModel): ViewModel
    }

    @Provides
    fun application(mainApp: MainApp): Application = mainApp

    @Provides
    @Singleton
    fun provideForecastDatabase(appContext: Application): ForecastDatabase {
        return Room.databaseBuilder(
                appContext,
                ForecastDatabase::class.java,
                "forecast_database"
            )
            .build()
    }

    @Provides
    @Singleton
    fun providesAppRetrofit(): Retrofit {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val logger = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                println(message)
            }
        })
        logger.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addInterceptor(logger)

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .baseUrl(API_ENDPOINT)
            .build()
    }

    @Provides
    @Singleton
    fun forecastApi(retrofit: Retrofit): ForecastApi = retrofit.create(ForecastApi::class.java)

    @Provides
    fun forecastDao(forecastDatabase: ForecastDatabase): ForecastDao = forecastDatabase.forecastDao()

    companion object {
        const val API_ENDPOINT = "https://api.openweathermap.org/data/2.5/"
    }
}