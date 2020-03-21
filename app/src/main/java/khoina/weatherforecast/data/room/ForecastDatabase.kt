package khoina.weatherforecast.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ForecastRecord::class], version = 1)
abstract class ForecastDatabase: RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}