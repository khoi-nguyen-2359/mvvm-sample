package khoina.weatherforecast

import java.util.*

data class ForecastModel(
    val date: Date,
    val aveTemp: Float,
    val pressure: Int,
    val humidity: Int,
    val description: String
)