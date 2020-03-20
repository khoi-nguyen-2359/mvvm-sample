package khoina.weatherforecast

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForecastRepository {
    private val forecastApi: ForecastApi
    private val entityModel = ForecastEntityMapper()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        forecastApi = retrofit.create(ForecastApi::class.java)
    }

    fun getForecastList(place: String, cnt: Int) = liveData<Resource<List<ForecastModel>>>(Dispatchers.IO) {
        emit(Resource.Loading(null))
        try {
            val response = forecastApi.getDailyForecast(place, cnt)
            val models = response.list.map(entityModel::map)
            emit(Resource.Success(models))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
        }
    }
}