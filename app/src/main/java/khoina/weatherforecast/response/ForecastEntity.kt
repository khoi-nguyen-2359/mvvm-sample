package khoina.weatherforecast.response

data class ForecastEntity(
    val dt: Long,
    val temp: DayTempEntity,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherEntity>
)
