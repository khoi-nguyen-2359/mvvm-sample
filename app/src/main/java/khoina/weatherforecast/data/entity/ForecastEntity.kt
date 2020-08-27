package khoina.weatherforecast.data.entity

import com.google.gson.annotations.SerializedName

data class ForecastEntity(
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("temp")
    val temp: DayTempEntity,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("weather")
    val weather: List<WeatherEntity>
)
