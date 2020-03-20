package khoina.weatherforecast

import khoina.weatherforecast.room.ForecastRecord
import java.util.*

class ForecastRecordMapper {
    fun map(record: ForecastRecord): ForecastModel {
        return ForecastModel(
            Date(record.date),
            record.aveTemp,
            record.pressure,
            record.humidity,
            record.description
        )
    }

    fun map(place: String, model: ForecastModel): ForecastRecord {
        return ForecastRecord(
            System.currentTimeMillis(),
            place,
            model.date.time,
            model.aveTemp,
            model.pressure,
            model.humidity,
            model.description
        )
    }
}