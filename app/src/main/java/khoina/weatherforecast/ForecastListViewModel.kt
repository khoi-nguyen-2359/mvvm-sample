package khoina.weatherforecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import khoina.weatherforecast.data.repo.ForecastRepositoryImpl
import khoina.weatherforecast.data.Resource
import khoina.weatherforecast.data.repo.ForecastRepository
import javax.inject.Inject


class ForecastListViewModel @Inject constructor(
    private val repository: ForecastRepository
): ViewModel() {

    private var forecastDataSource: LiveData<Resource<List<ForecastModel>>>? = null
    private val liveForecastData = MediatorLiveData<List<ForecastModel>>()
    fun getLiveForecastData() = liveForecastData as LiveData<List<ForecastModel>>

    private val error = MutableLiveData<Exception>()
    fun getError(): LiveData<Exception> = error

    private val isLoading = MutableLiveData<Boolean>()
    fun isLoading(): LiveData<Boolean> = isLoading

    fun getForecastList(place: String) {
        forecastDataSource ?. let { liveForecastData.removeSource(it) }
        forecastDataSource = repository.getForecastList(place, ITEM_COUNT)
            .also { liveDataSource ->
                liveForecastData.addSource(liveDataSource) { result ->
                    Log.d("khoi", "incoming data ${result.data?.size}")
                    liveForecastData.value = result.data ?: emptyList()

                    if (result is Resource.Error) {
                        error.value = result.exception
                    } else {
                        error.value = null
                    }

                    isLoading.value = result is Resource.Loading
                }
            }
    }

    companion object {
        const val ITEM_COUNT = 7
    }
}