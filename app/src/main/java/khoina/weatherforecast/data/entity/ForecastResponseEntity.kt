package khoina.weatherforecast.data.entity

import com.google.gson.annotations.SerializedName

data class ForecastResponseEntity(
    @SerializedName("list")
    val list: List<ForecastEntity>
)
