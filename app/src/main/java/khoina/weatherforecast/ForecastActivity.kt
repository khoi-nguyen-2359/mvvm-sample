package khoina.weatherforecast

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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

    private val forecastListObserver = Observer<Resource<List<ForecastModel>>> { resource ->
        if (resource is Resource.Loading)
            pbLoading.visibility = View.VISIBLE
        else
            pbLoading.visibility = View.INVISIBLE

        if (resource is Resource.Error)
            showErrorDialog(resource.exception.message)

        resource.data ?. let {
            forecastAdapter.submitData(it)
        }
    }

    private fun showErrorDialog(message: String?) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .show()
    }
}