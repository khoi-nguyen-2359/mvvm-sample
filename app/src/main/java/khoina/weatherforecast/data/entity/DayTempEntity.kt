package khoina.weatherforecast.data.entity

import com.google.gson.annotations.SerializedName

data class DayTempEntity(
    @SerializedName("day")
    val day: Float
)
