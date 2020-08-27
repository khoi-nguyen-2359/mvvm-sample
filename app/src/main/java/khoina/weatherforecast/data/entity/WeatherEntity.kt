package khoina.weatherforecast.data.entity

import com.google.gson.annotations.SerializedName

data class WeatherEntity(
    @SerializedName("description")
    val description: String
)
