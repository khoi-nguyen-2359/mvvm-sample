package khoina.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_weather_forecast.*

class ForecastActivity: AppCompatActivity(R.layout.activity_weather_forecast) {

    private val forecastAdapter = ForecastAdapter()
    private val forecastListViewModel by lazy { ViewModelProvider(this)[ForecastListViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupListView()

        forecastListViewModel.getForecastData().observe(this, Observer {
            forecastAdapter.submitData(it)
        })
    }

    private fun setupListView() {
        rcvWeatherForecast.adapter = forecastAdapter
        val dividerDrawable = resources.getDrawable(R.drawable.list_vertical_divider)
        rcvWeatherForecast.addItemDecoration(VerticalDividerDecoration(dividerDrawable))
    }
}