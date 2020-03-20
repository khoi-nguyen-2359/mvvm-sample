package khoina.weatherforecast

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_weather_forecast.*

class ForecastActivity: AppCompatActivity(R.layout.activity_weather_forecast) {

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ForecastListViewModel::class.java)) {
                return ForecastListViewModel(application) as T
            }

            throw IllegalArgumentException("unknown model class $modelClass")
        }

    }
    private val forecastAdapter = ForecastAdapter()
    private val forecastListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ForecastListViewModel::class.java]
    }

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
        Log.d("khoi", "resource count = ${resource.data?.size}")
        if (resource is Resource.Loading)
            pbLoading.visibility = View.VISIBLE
        else
            pbLoading.visibility = View.INVISIBLE

        if (resource is Resource.Error) {
            tvError.text = resource.exception.message
        } else {
            tvError.text = ""
        }

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