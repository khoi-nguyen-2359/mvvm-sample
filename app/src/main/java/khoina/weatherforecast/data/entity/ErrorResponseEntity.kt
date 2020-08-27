package khoina.weatherforecast.data.entity

import com.google.gson.annotations.SerializedName

data class ErrorResponseEntity(
    @SerializedName("cod")
    val cod: String,
    @SerializedName("message")
    val message: String
)