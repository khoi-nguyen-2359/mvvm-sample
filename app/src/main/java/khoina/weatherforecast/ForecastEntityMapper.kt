package khoina.weatherforecast

import khoina.weatherforecast.response.DayForecastEntity
import java.util.*

class ForecastEntityMapper {
    fun map(entity: DayForecastEntity): ForecastModel {
        return ForecastModel(
            Date(entity.dt * 1000),
            (entity.temp.min + entity.temp.max) / 2,
            entity.pressure,
            entity.humidity,
            entity.weather.firstOrNull()?.description ?: ""
        )
    }
}