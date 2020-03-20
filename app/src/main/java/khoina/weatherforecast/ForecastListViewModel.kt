package khoina.weatherforecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ForecastListViewModel: ViewModel() {

    private val forecastData = MutableLiveData<List<ForecastModel>>()
    fun getForecastData() = forecastData as LiveData<List<ForecastModel>>

    init {
        forecastData.value = listOf(
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
    }
}