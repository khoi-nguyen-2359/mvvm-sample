package khoina.weatherforecast.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ForecastDao {
    @Insert
    fun insertForecastList(forecastData: List<ForecastRecord>)

    @Query("SELECT * FROM forecast WHERE place = :place")
    fun getForecastList(place: String): LiveData<List<ForecastRecord>>

    @Query("SELECT count(*) FROM forecast WHERE place = :place AND createdAt >= :time")
    fun countFreshData(place: String, time: Long): Int

    fun hasFreshData(place: String, count: Int, time: Long) = countFreshData(place, time) == count

    @Transaction
    fun replaceAllForecast(place: String, forecast: List<ForecastRecord>) {
        clearForecast(place)
        insertForecastList(forecast)
    }

    @Query("DELETE FROM forecast WHERE place = :place")
    fun clearForecast(place: String)
}