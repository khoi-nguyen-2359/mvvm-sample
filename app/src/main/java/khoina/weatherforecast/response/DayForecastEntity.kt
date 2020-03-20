package khoina.weatherforecast.response

data class DayForecastEntity(
    val dt: Long,
    val temp: DayTempEntity,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherEntity>
)
