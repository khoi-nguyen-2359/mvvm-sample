package khoina.weatherforecast

import khoina.weatherforecast.data.ForecastEntityMapper
import khoina.weatherforecast.data.room.ForecastRecord
import org.junit.Assert
import org.junit.Test

class ModelMappingUnitTest {
    @Test
    fun modelMapping_isCorrectDateConversion() {
        val mapper = ForecastEntityMapper()
        val dateInSec = 1588219524L
        val record = ForecastRecord(
            "",
            dateInSec,
            0f,
            0,
            0,
            ""
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.date, dateInSec)
        Assert.assertEquals(model.date.time, dateInSec * 1000)
    }

    @Test
    fun modelMapping_isCorrectAveTempCalculation() {
        val mapper = ForecastEntityMapper()
        val aveTemp = 32f
        val record = ForecastRecord(
            "",
            0,
            aveTemp,
            0,
            0,
            ""
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.aveTemp, aveTemp)
        Assert.assertEquals(model.aveTemp, aveTemp)
    }

    @Test
    fun modelMapping_isCorrectNegativeAveTempCalculation() {
        val mapper = ForecastEntityMapper()
        val aveTemp = -18f
        val record = ForecastRecord(
            "",
            0,
            aveTemp,
            0,
            0,
            ""
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.aveTemp, aveTemp)
        Assert.assertEquals(model.aveTemp, aveTemp)
    }

    @Test
    fun modelMapping_isCorrectPressure() {
        val mapper = ForecastEntityMapper()
        val pressure = 1900
        val record = ForecastRecord(
            "",
            0,
            0f,
            pressure,
            0,
            ""
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.pressure, pressure)
        Assert.assertEquals(model.pressure, pressure)
    }

    @Test
    fun modelMapping_isCorrectArbitraryPressure() {
        val mapper = ForecastEntityMapper()
        val pressure = -1900733
        val record = ForecastRecord(
            "",
            0,
            0f,
            pressure,
            0,
            ""
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.pressure, pressure)
        Assert.assertEquals(model.pressure, pressure)
    }

    @Test
    fun modelMapping_isCorrectArbitraryHumidity() {
        val mapper = ForecastEntityMapper()
        val humidity = -1060
        val record = ForecastRecord(
            "",
            0,
            0f,
            0,
            humidity,
            ""
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.humidity, humidity)
        Assert.assertEquals(model.humidity, humidity)
    }

    @Test
    fun modelMapping_isCorrectHumidity() {
        val mapper = ForecastEntityMapper()
        val humidity = 60
        val record = ForecastRecord(
            "",
            0,
            0f,
            0,
            humidity,
            ""
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.humidity, humidity)
        Assert.assertEquals(model.humidity, humidity)
    }

    @Test
    fun modelMapping_isCorrectWeatherDescription() {
        val mapper = ForecastEntityMapper()
        val weatherDescription = "light rain"
        val record = ForecastRecord(
            "",
            0,
            0f,
            0,
            0,
            weatherDescription
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.description, weatherDescription)
        Assert.assertEquals(model.description, weatherDescription)
    }

    @Test
    fun modelMapping_isCorrectEmptyWeatherDescription() {
        val mapper = ForecastEntityMapper()
        val weatherDescription = ""
        val record = ForecastRecord(
            "",
            0,
            0f,
            0,
            0,
            weatherDescription
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.description, weatherDescription)
        Assert.assertEquals(model.description, weatherDescription)
    }

    @Test
    fun modelMapping_isCorrectArbitraryWeatherDescription() {
        val mapper = ForecastEntityMapper()
        val weatherDescription = "!)(U mm  "
        val record = ForecastRecord(
            "",
            0,
            0f,
            0,
            0,
            weatherDescription
        )
        val model = mapper.mapModel(record)
        Assert.assertEquals(record.description, weatherDescription)
        Assert.assertEquals(model.description, weatherDescription)
    }
}