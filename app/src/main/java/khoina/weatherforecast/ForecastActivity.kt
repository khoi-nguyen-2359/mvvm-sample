package khoina.weatherforecast

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_weather_forecast.*

class ForecastActivity: AppCompatActivity(R.layout.activity_weather_forecast) {

    private val forecastAdapter = ForecastAdapter()
    private val forecastListViewModel by lazy { ViewModelProvider(this)[ForecastListViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupObservers() {
        forecastListViewModel.getLiveForecastData().observe(this, forecastListObserver)
    }

    private fun setupViews() {
        rcvWeatherForecast.adapter = forecastAdapter
        val dividerDrawable = resources.getDrawable(R.drawable.list_vertical_divider)
        rcvWeatherForecast.addItemDecoration(VerticalDividerDecoration(dividerDrawable))

        btGetWeather.setOnClickListener(onClickGetWeather)
    }

    private val onClickGetWeather = View.OnClickListener {
        val place = etPlace.text.toString().trim()
        forecastListViewModel.getForecastList(place)
    }

    private val forecastListObserver = Observer<List<ForecastModel>> {
        forecastAdapter.submitData(it)
    }
}