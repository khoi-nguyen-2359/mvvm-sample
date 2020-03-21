package khoina.weatherforecast

import android.app.Application
import androidx.lifecycle.*
import khoina.weatherforecast.data.ForecastRepository
import khoina.weatherforecast.data.Resource
import javax.inject.Inject


class ForecastListViewModel @Inject constructor(
    private val repository: ForecastRepository
): ViewModel() {

    private var forecastDataSource: LiveData<Resource<List<ForecastModel>>>? = null
    private val liveForecastData = MediatorLiveData<Resource<List<ForecastModel>>>()
    fun getLiveForecastData() = liveForecastData as LiveData<Resource<List<ForecastModel>>>

    fun getForecastList(place: String) {
        forecastDataSource ?. let { liveForecastData.removeSource(it) }
        forecastDataSource = repository.getForecastList(place, ITEM_COUNT)
            .also { liveDataSource ->
                liveForecastData.addSource(liveDataSource) { result ->
                    liveForecastData.value = result
                }
            }
    }

    companion object {
        const val ITEM_COUNT = 16
    }
}