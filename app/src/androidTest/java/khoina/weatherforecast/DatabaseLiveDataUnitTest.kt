package khoina.weatherforecast

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import khoina.weatherforecast.data.room.ForecastDao
import khoina.weatherforecast.data.room.ForecastDatabase
import khoina.weatherforecast.data.room.ForecastRecord
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseLiveDataUnitTest {
    private lateinit var database: ForecastDatabase
    private lateinit var forecastDao: ForecastDao

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, ForecastDatabase::class.java).build()
        forecastDao = database.forecastDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun database_insertOneItem() {
        forecastDao.insertForecast(
            "saigon",
            1588179600L,
            30f,
            1001,
            60,
            "light rain"
        )
        val forecastLiveData = forecastDao.getForecastList("saigon")
        val forecastList = forecastLiveData.getOrAwaitValue()
        assertEquals(forecastList.size, 1)
        assertEquals(forecastList.first().place, "saigon")
        assertEquals(forecastList.first().date, 1588179600L)
        assertEquals(forecastList.first().aveTemp, 30f)
        assertEquals(forecastList.first().pressure, 1001)
        assertEquals(forecastList.first().humidity, 60)
        assertEquals(forecastList.first().description, "light rain")
    }

    @Test
    fun database_insertArbitraryItem() {
        forecastDao.insertForecast(
            ")(*  ",
            0,
            -100f,
            -1,
            600,
            ""
        )
        val forecastLiveData = forecastDao.getForecastList(")(*  ")
        val forecastList = forecastLiveData.getOrAwaitValue()
        assertEquals(forecastList.size, 1)
        assertEquals(forecastList.first().place, ")(*  ")
        assertEquals(forecastList.first().date, 0)
        assertEquals(forecastList.first().aveTemp, -100f)
        assertEquals(forecastList.first().pressure, -1)
        assertEquals(forecastList.first().humidity, 600)
        assertEquals(forecastList.first().description, "")
    }

    @Test
    fun database_insertMultipleItems() {
        forecastDao.insertForecast(
            "saigon",
            1588179600L,
            30f,
            1001,
            60,
            "light rain"
        )
        forecastDao.insertForecast(
            "saigon",
            1588266000,
            10f,
            1001,
            30,
            "cloudy"
        )
        val forecastLiveData = forecastDao.getForecastList("saigon")
        val forecastList = forecastLiveData.getOrAwaitValue()
        assertEquals(forecastList.size, 2)
    }

    @Test
    fun database_successfullyClearDatabase() {
        forecastDao.insertForecast(
            "saigon",
            1588179600L,
            30f,
            1001,
            60,
            "light rain"
        )
        forecastDao.insertForecast(
            "saigon",
            1588266000,
            20f,
            1001,
            30,
            "cloudy"
        )
        forecastDao.clearForecast("saigon")
        val forecastLiveData = forecastDao.getForecastList("saigon")
        val forecastList = forecastLiveData.getOrAwaitValue()
        assertEquals(forecastList.size, 0)
    }

    @Test
    fun database_successfullyClearDatabaseWithDiffPlaces() {
        forecastDao.insertForecast(
            "saigon",
            1588179600L,
            30f,
            1001,
            60,
            "light rain"
        )
        forecastDao.insertForecast(
            "new york",
            1588266000,
            20f,
            1001,
            30,
            "cloudy"
        )
        forecastDao.clearForecast("saigon")
        var forecastLiveData = forecastDao.getForecastList("saigon")
        var forecastList = forecastLiveData.getOrAwaitValue()
        assertEquals(forecastList.size, 0)

        forecastLiveData = forecastDao.getForecastList("new york")
        forecastList = forecastLiveData.getOrAwaitValue()
        assertEquals(forecastList.size, 1)
    }

    @Test
    fun database_isCorrectReplacingData() {
        forecastDao.insertForecast(
            "saigon",
            1588179600L,
            30f,
            1001,
            60,
            "light rain"
        )
        forecastDao.insertForecast(
            "saigon",
            1588266000,
            20f,
            1001,
            30,
            "cloudy"
        )
        val replacingItems = listOf(
            ForecastRecord("new york",
                1588179600L,
                30f,
                1001,
                60,
                "light rain")
        )
        forecastDao.replaceAllForecast("saigon", replacingItems)
        var liveData = forecastDao.getForecastList("new york")
        var liveDataValue = liveData.getOrAwaitValue()
        assertEquals(liveDataValue.size, 1)

        liveData = forecastDao.getForecastList("saigon")
        liveDataValue = liveData.getOrAwaitValue()
        assertEquals(liveDataValue.size, 0)
    }

    @Test
    fun database_isCorrectReplacingWithExistingPlaceData() {
        forecastDao.insertForecast(
            "saigon",
            1588179600L,
            30f,
            1001,
            60,
            "light rain"
        )
        forecastDao.insertForecast(
            "new york",
            1588266000,
            20f,
            1001,
            30,
            "cloudy"
        )
        val replacingItems = listOf(
            ForecastRecord("new york",
                1588179600L,
                30f,
                1001,
                60,
                "light rain")
        )
        forecastDao.replaceAllForecast("saigon", replacingItems)
        var liveData = forecastDao.getForecastList("saigon")
        var liveDataValue = liveData.getOrAwaitValue()
        assertEquals(liveDataValue.size, 0)

        liveData = forecastDao.getForecastList("new york")
        liveDataValue = liveData.getOrAwaitValue()
        assertEquals(liveDataValue.size, 2)
    }

    @Test
    fun database_isCorrectReplacingWithEmptyData() {
        forecastDao.insertForecast(
            "saigon",
            1588179600L,
            30f,
            1001,
            60,
            "light rain"
        )
        forecastDao.insertForecast(
            "saigon",
            1588266000,
            20f,
            1001,
            30,
            "cloudy"
        )
        val replacingItems = listOf<ForecastRecord>()
        forecastDao.replaceAllForecast("saigon", replacingItems)
        val liveData = forecastDao.getForecastList("saigon")
        val liveDataValue = liveData.getOrAwaitValue()
        assertEquals(liveDataValue.size, 0)
    }
}