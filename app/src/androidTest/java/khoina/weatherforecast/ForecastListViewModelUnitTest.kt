package khoina.weatherforecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import khoina.weatherforecast.data.repo.ForecastRepositoryImpl
import khoina.weatherforecast.data.ParsedHttpException
import khoina.weatherforecast.data.Resource
import khoina.weatherforecast.data.entity.ErrorResponseEntity
import khoina.weatherforecast.data.retrofit.ForecastApi
import khoina.weatherforecast.data.room.ForecastDatabase
import khoina.weatherforecast.manager.AppConfigManager
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
@LargeTest
class ForecastListViewModelUnitTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var appConfigManager: AppConfigManager

    private lateinit var database: ForecastDatabase

    lateinit var mockServer: MockWebServer
    private val application = ApplicationProvider.getApplicationContext<MainApp>()
    lateinit var viewModel: ForecastListViewModel

    @Before
    fun setup() {
        mockServer = MockWebServer()
        mockServer.start()

        val gson = application.getAppComponent().gson()
        val retrofit = application.getAppComponent().retrofitBuilder()
            .baseUrl(mockServer.url("/").toString())
            .build()

        val mockForecastApi = retrofit.create(ForecastApi::class.java)

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), ForecastDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val forecastDao = database.forecastDao()

        appConfigManager = AppConfigManager()
        val repository = ForecastRepositoryImpl(
            mockForecastApi,
            forecastDao,
            gson,
            appConfigManager
        )

        viewModel = ForecastListViewModel(repository)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun viewModel_isCorrectInitForecastData() {
        val liveForecastData = viewModel.getLiveForecastData()
        try {
            liveForecastData.getOrAwaitValue()
        } catch (ex: Exception) {
            assertTrue(ex is TimeoutException)
            return
        }

        assertTrue(false)
    }

    @Test
    fun viewModel_isCorrectGettingItemSizeOfSaigonWeather() {
        mockServer.enqueue(
            MockResponse()
                .setBodyDelay(1, TimeUnit.SECONDS)
                .setResponseCode(200)
                .setBody(readResponseFromFile("saigon_7days_forecast.json"))
        )

        val liveForecastData = viewModel.getLiveForecastData()
        viewModel.getForecastList("saigon")
        val data1 = liveForecastData.getOrAwaitValue()
        assertTrue(data1 is Resource.Loading)

        Thread.sleep(2000)

        val data2 = liveForecastData.getOrAwaitValue()
        assertTrue(data2 is Resource.Success)
        assertEquals(data2.data?.size, 7)
    }

    @Test
    fun viewModel_isCorrectGettingItemDataOfNewyorknWeatherForecast() {
        mockServer.enqueue(
            MockResponse()
                .setBodyDelay(1, TimeUnit.SECONDS)
                .setResponseCode(200)
                .setBody(readResponseFromFile("newyork_3days_forecast.json"))
        )

        val liveForecastData = viewModel.getLiveForecastData()
        viewModel.getForecastList("saigon")
        val data1 = liveForecastData.getOrAwaitValue()
        assertTrue(data1 is Resource.Loading)

        Thread.sleep(2000)

        val data2 = liveForecastData.getOrAwaitValue()
        assertTrue(data2 is Resource.Success)
        assertEquals(data2.data?.size, 3)

        val mockedItems = listOf(
            ForecastModel(Date(1584896400000), 4.95f, 1038, 63, "overcast clouds"),
            ForecastModel(Date(1584982800000), 6.13f, 1028, 87, "heavy intensity rain"),
            ForecastModel(Date(1585069200000), 8.17f, 1022, 81, "overcast clouds")
        )
        data2.data?.forEachIndexed { index, item ->
            assertEquals(item, mockedItems[index])
        }
    }

    @Test
    fun viewModel_isCorrectCachingWeatherForecast() {
        appConfigManager.setCacheDuration(10)
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(readResponseFromFile("saigon_7days_forecast.json"))
        )

        val liveForecastData = viewModel.getLiveForecastData()
        viewModel.getForecastList("saigon")
        liveForecastData.getOrAwaitValue()
        Thread.sleep(1000)
        val data1 = liveForecastData.getOrAwaitValue()

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(readResponseFromFile("newyork_3days_forecast.json"))
        )
        viewModel.getForecastList("new york")
        liveForecastData.getOrAwaitValue()
        Thread.sleep(1000)
        val data2 = liveForecastData.getOrAwaitValue()

        viewModel.getForecastList("saigon")
        liveForecastData.getOrAwaitValue()
        Thread.sleep(1000)
        val data3 = liveForecastData.getOrAwaitValue()

        assertTrue(data1 is Resource.Success)
        assertTrue(data2 is Resource.Success)
        assertTrue(data3 is Resource.Success)

        data1.data?.forEachIndexed { index, item ->
            assertTrue(item == data3.data!![index])
        }
    }

    @Test
    fun viewModel_isCorrectErrorGettingWeather() {
        mockServer.enqueue(
            MockResponse()
                .setBodyDelay(1, TimeUnit.SECONDS)
                .setResponseCode(404)
                .setBody(readResponseFromFile("404_city_not_found.json"))
        )

        val liveForecastData = viewModel.getLiveForecastData()
        viewModel.getForecastList("saigon2")
        val data1 = liveForecastData.getOrAwaitValue()
        assertTrue(data1 is Resource.Loading)

        Thread.sleep(2000)

        val data2 = liveForecastData.getOrAwaitValue()
        assertTrue(data2 is Resource.Error)
        assertTrue(data2.data == null)
        assertTrue((data2 as Resource.Error).exception is ParsedHttpException)
        assertTrue((data2.exception as ParsedHttpException).errorResponse == ErrorResponseEntity("404", "city not found"))
    }

}