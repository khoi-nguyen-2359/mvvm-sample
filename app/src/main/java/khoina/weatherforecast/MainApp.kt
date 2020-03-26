package khoina.weatherforecast

import android.app.Application
import khoina.weatherforecast.dagger.AppComponent
import khoina.weatherforecast.dagger.DaggerAppComponent


class MainApp : Application() {
	private var appComponent = DaggerAppComponent.builder()
		.mainApp(this)
		.build()

	fun getAppComponent() = appComponent
}