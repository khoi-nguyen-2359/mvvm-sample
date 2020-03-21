package com.media2359.genflix

import android.app.Application
import com.media2359.genflix.dagger.DaggerAppComponent


class MainApp : Application() {
	val appComponent = DaggerAppComponent.builder()
		.mainApp(this)
		.build()
}