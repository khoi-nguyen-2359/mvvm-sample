package khoina.weatherforecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ForecastListViewModel: ViewModel() {

    private val entityMapper = ForecastEntityMapper()
    private val forecastApi: ForecastApi
    private val liveForecastData = MutableLiveData<List<ForecastModel>>()
    fun getLiveForecastData() = liveForecastData as LiveData<List<ForecastModel>>

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        forecastApi = retrofit.create(ForecastApi::class.java)
    }

    fun getForecastList(place: String) {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val forecastResponse = forecastApi.getDailyForecast(place, ITEM_COUNT)
                val modelData = forecastResponse.list.map(entityMapper::map)
                liveForecastData.postValue(modelData)
            }
        }
    }

    companion object {
        const val ITEM_COUNT = 16
    }
}