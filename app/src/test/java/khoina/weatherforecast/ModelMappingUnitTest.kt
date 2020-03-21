package khoina.weatherforecast

import khoina.weatherforecast.data.ForecastEntityMapper
import khoina.weatherforecast.data.room.ForecastRecord
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class ModelMappingUnitTest {
    @Test
    fun modelMapping_isCorrectMapping() {
        val mapper = ForecastEntityMapper()
        val record = ForecastRecord(
            "saigon",
            1588219524L,
            30f,
            1020,
            30,
            "scattered clouds"
        )
        val model = mapper.mapModel(record)
        assertEquals(model.date.time, 1588219524000)
        assertEquals(model.aveTemp, 30f)
        assertEquals(model.pressure, 1020)
        assertEquals(model.humidity, 30)
        assertEquals(model.description, "scattered clouds")
    }

    @Test
    fun modelMapping_isCorrectArbitraryPlaceMapping() {
        val mapper = ForecastEntityMapper()
        val record = ForecastRecord(
            "!)* m",
            1588219524L,
            -10f,
            1011,
            0,
            "scattered clouds"
        )
        val model = mapper.mapModel(record)
        assertEquals(model.date.time, 1588219524000)
        assertEquals(model.aveTemp, -10f)
        assertEquals(model.pressure, 1011)
        assertEquals(model.humidity, 0)
        assertEquals(model.description, "scattered clouds")
    }

    @Test
    fun modelMapping_isCorrectArbitraryDateMapping() {
        val mapper = ForecastEntityMapper()
        val record = ForecastRecord(
            "!)* m",
            0,
            -10f,
            1011,
            0,
            "sky is clear"
        )
        val model = mapper.mapModel(record)
        assertEquals(model.date.time, 0)
        assertEquals(model.aveTemp, -10f)
        assertEquals(model.pressure, 1011)
        assertEquals(model.humidity, 0)
        assertEquals(model.description, "sky is clear")
    }

    @Test
    fun modelMapping_isCorrectEmptyDescriptionMapping() {
        val mapper = ForecastEntityMapper()
        val record = ForecastRecord(
            "!)* m",
            0,
            -10f,
            1011,
            0,
            ""
        )
        val model = mapper.mapModel(record)
        assertEquals(model.date.time, 0)
        assertEquals(model.aveTemp, -10f)
        assertEquals(model.pressure, 1011)
        assertEquals(model.humidity, 0)
        assertEquals(model.description, "")
    }

    @Test
    fun modelMapping_isCorrectArbitraryDataMapping() {
        val mapper = ForecastEntityMapper()
        val record = ForecastRecord(
            ")!(N ",
            0,
            -300f,
            -12,
            -999,
            ""
        )
        val model = mapper.mapModel(record)
        assertEquals(model.date.time, 0)
        assertEquals(model.aveTemp, -300f)
        assertEquals(model.pressure, -12)
        assertEquals(model.humidity, -999)
        assertEquals(model.description, "")
    }
}