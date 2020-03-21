package khoina.weatherforecast

import khoina.weatherforecast.response.ForecastEntity
import khoina.weatherforecast.room.ForecastRecord
import java.util.*

class ForecastEntityMapper {
    fun mapRecord(place: String, entity: ForecastEntity): ForecastRecord {
        return ForecastRecord(
            place,
            entity.dt,
            (entity.temp.min + entity.temp.max) / 2,
            entity.pressure,
            entity.humidity,
            entity.weather.firstOrNull()?.description ?: ""
        )
    }

    fun mapModel(record: ForecastRecord): ForecastModel {
        return ForecastModel(
            Date(record.date * 1000),
            record.aveTemp,
            record.pressure,
            record.humidity,
            record.description
        )
    }
}