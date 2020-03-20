package khoina.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_weather_forecast.*
import java.util.*

class ForecastActivity: AppCompatActivity(R.layout.activity_weather_forecast) {

    private val forecastAdapter = ForecastAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupListView()
    }

    private fun setupListView() {
        rcvWeatherForecast.adapter = forecastAdapter
        val dividerDrawable = resources.getDrawable(R.drawable.list_vertical_divider)
        rcvWeatherForecast.addItemDecoration(VerticalDividerDecoration(dividerDrawable))

        val testData = listOf(
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain"),
            ForecastModel(Date(), 30f, 30, 30, "light rain")
        )
        forecastAdapter.setData(testData)
    }
}