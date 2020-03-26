package khoina.weatherforecast.dagger

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import khoina.weatherforecast.MainApp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import khoina.weatherforecast.ForecastListViewModel
import khoina.weatherforecast.data.repo.ForecastRepository
import khoina.weatherforecast.data.repo.ForecastRepositoryImpl
import khoina.weatherforecast.data.retrofit.ForecastApi
import khoina.weatherforecast.data.room.ForecastDao
import khoina.weatherforecast.data.room.ForecastDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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

        @Binds
        @Singleton
        fun forecastRepo(repositoryImpl: ForecastRepositoryImpl): ForecastRepository
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
    fun gson(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create()

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val logger = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                println(message)
            }
        })
        logger.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addInterceptor(logger)

        return okHttpClientBuilder.build()
    }

    @Provides
    fun providesAppRetrofitBuilder(gson: Gson, okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(API_ENDPOINT)
    }

    @Provides
    @Singleton
    fun providesAppRetrofit(retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.build()
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