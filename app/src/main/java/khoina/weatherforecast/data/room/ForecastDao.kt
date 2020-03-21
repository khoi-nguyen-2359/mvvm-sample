package khoina.weatherforecast.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ForecastDao {
    @Query("INSERT OR ABORT INTO forecast (place,date,aveTemp,pressure,humidity,description,createdAtSec) VALUES (:place,:date,:aveTemp,:pressure,:humidity,:description,strftime('%s','now'))")
    fun insertForecast(place: String, date: Long, aveTemp: Float, pressure: Int, humidity: Int, description: String)

    @Query("SELECT * FROM forecast WHERE place = :place")
    fun getForecastList(place: String): LiveData<List<ForecastRecord>>

    @Query("SELECT count(*) FROM forecast WHERE place = :place AND createdAtSec >= :staleTime")
    fun countFreshData(place: String, staleTime: Long): Int

    fun hasFreshData(place: String, count: Int, time: Long) = countFreshData(place, time) == count

    @Transaction
    fun replaceAllForecast(place: String, forecastList: List<ForecastRecord>) {
        clearForecast(place)
        forecastList.forEach {
            insertForecast(
                it.place,
                it.date,
                it.aveTemp,
                it.pressure,
                it.humidity,
                it.description
            )
        }
    }

    @Query("DELETE FROM forecast WHERE place = :place")
    fun clearForecast(place: String)
}