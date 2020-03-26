package khoina.weatherforecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import khoina.weatherforecast.data.repo.ForecastRepositoryImpl
import khoina.weatherforecast.data.Resource
import javax.inject.Inject


class ForecastListViewModel @Inject constructor(
    private val repository: ForecastRepositoryImpl
): ViewModel() {

    private var forecastDataSource: LiveData<Resource<List<ForecastModel>>>? = null
    private val liveForecastData = MediatorLiveData<Resource<List<ForecastModel>>>()
    fun getLiveForecastData() = liveForecastData as LiveData<Resource<List<ForecastModel>>>

    fun getForecastList(place: String) {
        forecastDataSource ?. let { liveForecastData.removeSource(it) }
        forecastDataSource = repository.getForecastList(place, ITEM_COUNT)
            .also { liveDataSource ->
                liveForecastData.addSource(liveDataSource) { result ->
                    Log.d("khoi", "incoming data ${result.data?.size}")
                    liveForecastData.value = result
                }
            }
    }

    companion object {
        const val ITEM_COUNT = 7
    }
}