package khoina.weatherforecast

import khoina.weatherforecast.response.ForecastResponseEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {
    @GET("forecast/daily?appid=60c6fbeb4b93ac653c492ba806fc346d&units=metric")
    suspend fun getDailyForecast(@Query("q") place: String, @Query("cnt") count: Int): ForecastResponseEntity
}