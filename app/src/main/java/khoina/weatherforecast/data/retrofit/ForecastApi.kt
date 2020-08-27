package khoina.weatherforecast.data.retrofit

import khoina.weatherforecast.data.entity.ForecastResponseEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {
    @GET("forecast/daily?appid=4c89d4f6b77b3be3e03bd20cd44b6903&units=metric")
    suspend fun getDailyForecast(@Query("q") place: String, @Query("cnt") count: Int): ForecastResponseEntity
}