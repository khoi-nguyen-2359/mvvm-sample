package khoina.weatherforecast

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import khoina.weatherforecast.data.room.ForecastDao
import khoina.weatherforecast.data.room.ForecastDatabase
import khoina.weatherforecast.data.room.ForecastRecord
import org.junit.*
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DatabaseUnitTest {
    private lateinit var database: ForecastDatabase
    private lateinit var forecastDao: ForecastDao

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
    fun database_availableFreshData() {
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
        val hasFreshData = forecastDao.hasFreshData("saigon", 2, System.currentTimeMillis() / 1000 - TimeUnit.DAYS.toSeconds(1))
        assertEquals(hasFreshData, true)
    }

    @Test
    fun database_unavailableFreshData() {
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
        val hasFreshData = forecastDao.hasFreshData("saigon", 2, System.currentTimeMillis() / 1000 + TimeUnit.DAYS.toSeconds(1))
        assertEquals(hasFreshData, false)
    }

    @Test
    fun database_unavailableFreshDataByDifferentPlace() {
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
        val hasFreshData = forecastDao.hasFreshData("new york", 2, System.currentTimeMillis() / 1000 - TimeUnit.DAYS.toSeconds(1))
        assertEquals(hasFreshData, false)
    }

    @Test
    fun database_isPartialUnavailableFreshData() {
        forecastDao.insertForecast(
            "saigon",
            1588179600L,
            30f,
            1001,
            60,
            "light rain"
        )
        Thread.sleep(3000)
        forecastDao.insertForecast(
            "saigon",
            1588266000,
            20f,
            1001,
            30,
            "cloudy"
        )
        val refreshDataCount = forecastDao.countFreshData("saigon", System.currentTimeMillis() / 1000 - 2)
        assertEquals(refreshDataCount, 1)
    }
}