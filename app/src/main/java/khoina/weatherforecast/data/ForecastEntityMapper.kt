package khoina.weatherforecast.data

import khoina.weatherforecast.data.entity.ForecastEntity
import khoina.weatherforecast.data.room.ForecastRecord
import khoina.weatherforecast.ForecastModel
import java.util.*

class ForecastEntityMapper {
    fun mapRecord(place: String, entity: ForecastEntity): ForecastRecord {
        return ForecastRecord(
            place,
            entity.dt,
            entity.temp.day,
            entity.pressure,
            entity.humidity,
            entity.weather.firstOrNull()?.description ?: ""
        )
    }

    fun mapModel(record: ForecastRecord): ForecastModel {
        return ForecastModel(
            record.place,
            Date(record.date * 1000),
            record.aveTemp,
            record.pressure,
            record.humidity,
            record.description
        )
    }
}