package khoina.weatherforecast.dagger

import com.google.gson.Gson
import khoina.weatherforecast.MainApp
import dagger.BindsInstance
import dagger.Component
import khoina.weatherforecast.view.ForecastActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class
])
interface AppComponent {

    fun inject(forecastActivity: ForecastActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun mainApp(application: MainApp): Builder
    }

    fun okHttpClient(): OkHttpClient
    fun gson(): Gson
    fun retrofitBuilder(): Retrofit.Builder
}