package com.media2359.genflix.dagger

import com.media2359.genflix.MainApp
import dagger.BindsInstance
import dagger.Component
import khoina.weatherforecast.view.ForecastActivity
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

}