package khoina.weatherforecast.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastRecord(
    val createdAt: Long,
    val place: String,
    val date: Long,
    val aveTemp: Float,
    val pressure: Int,
    val humidity: Int,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)