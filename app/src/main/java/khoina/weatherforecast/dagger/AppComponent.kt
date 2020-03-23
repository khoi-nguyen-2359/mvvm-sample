package khoina.weatherforecast.dagger

import androidx.annotation.VisibleForTesting
import khoina.weatherforecast.MainApp
import dagger.BindsInstance
import dagger.Component
import khoina.weatherforecast.view.ForecastActivity
import okhttp3.OkHttpClient
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

    @VisibleForTesting
    fun okHttpClient(): OkHttpClient
}