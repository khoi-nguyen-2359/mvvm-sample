package khoina.weatherforecast

import khoina.weatherforecast.data.entity.DayTempEntity
import khoina.weatherforecast.data.entity.ForecastEntity
import khoina.weatherforecast.data.ForecastEntityMapper
import khoina.weatherforecast.data.entity.WeatherEntity
import org.junit.Assert
import org.junit.Test

class RecordMappingUnitTest {
    @Test
    fun recordMapping_isCorrectMapping() {
        val mapper = ForecastEntityMapper()
        val entity = ForecastEntity(
            1588219524L,
            DayTempEntity(10f),
            1000,
            30,
            listOf(WeatherEntity("Light rain"))
        )
        val record = mapper.mapRecord("Saigon", entity)
        Assert.assertEquals(record.place, "Saigon")
        Assert.assertEquals(record.date, 1588219524L)
        Assert.assertEquals(record.aveTemp, 10f)
        Assert.assertEquals(record.pressure, 1000)
        Assert.assertEquals(record.humidity, 30)
        Assert.assertEquals(record.description, "Light rain")
    }

    @Test
    fun recordMapping_isCorrectEmptyPlaceMapping() {
        val mapper = ForecastEntityMapper()
        val entity = ForecastEntity(
            1588219524L,
            DayTempEntity(10f),
            1000,
            30,
            listOf(WeatherEntity("Light rain"))
        )
        val record = mapper.mapRecord("", entity)
        Assert.assertEquals(record.place, "")
        Assert.assertEquals(record.date, 1588219524L)
        Assert.assertEquals(record.aveTemp, 10f)
        Assert.assertEquals(record.pressure, 1000)
        Assert.assertEquals(record.humidity, 30)
        Assert.assertEquals(record.description, "Light rain")
    }

    @Test
    fun recordMapping_isCorrectArbitraryDateMapping() {
        val mapper = ForecastEntityMapper()
        val entity = ForecastEntity(
            0,
            DayTempEntity(10f),
            1000,
            30,
            listOf(WeatherEntity("Light rain"))
        )
        val record = mapper.mapRecord("new york", entity)
        Assert.assertEquals(record.place, "new york")
        Assert.assertEquals(record.date, 0)
        Assert.assertEquals(record.aveTemp, 10f)
        Assert.assertEquals(record.pressure, 1000)
        Assert.assertEquals(record.humidity, 30)
        Assert.assertEquals(record.description, "Light rain")
    }

    @Test
    fun recordMapping_isCorrectEmptyDescriptionMapping() {
        val mapper = ForecastEntityMapper()
        val entity = ForecastEntity(
            0,
            DayTempEntity(10f),
            1000,
            30,
            listOf()
        )
        val record = mapper.mapRecord("new york", entity)
        Assert.assertEquals(record.place, "new york")
        Assert.assertEquals(record.date, 0)
        Assert.assertEquals(record.aveTemp, 10f)
        Assert.assertEquals(record.pressure, 1000)
        Assert.assertEquals(record.humidity, 30)
        Assert.assertEquals(record.description, "")
    }

    @Test
    fun recordMapping_isCorrectSingleDescriptionMapping() {
        val mapper = ForecastEntityMapper()
        val entity = ForecastEntity(
            0,
            DayTempEntity(10f),
            1000,
            30,
            listOf(WeatherEntity("light rain"), WeatherEntity("sky is clear"))
        )
        val record = mapper.mapRecord("new york", entity)
        Assert.assertEquals(record.place, "new york")
        Assert.assertEquals(record.date, 0)
        Assert.assertEquals(record.aveTemp, 10f)
        Assert.assertEquals(record.pressure, 1000)
        Assert.assertEquals(record.humidity, 30)
        Assert.assertEquals(record.description, "light rain")
    }


    @Test
    fun recordMapping_isCorrectArbitraryDataMapping() {
        val mapper = ForecastEntityMapper()
        val entity = ForecastEntity(
            0,
            DayTempEntity(-100f),
            -10,
            200,
            listOf()
        )
        val record = mapper.mapRecord("", entity)
        Assert.assertEquals(record.place, "")
        Assert.assertEquals(record.date, 0)
        Assert.assertEquals(record.aveTemp, -100f)
        Assert.assertEquals(record.pressure, -10)
        Assert.assertEquals(record.humidity, 200)
        Assert.assertEquals(record.description, "")
    }
}