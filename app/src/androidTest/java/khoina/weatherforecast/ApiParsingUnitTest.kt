package khoina.weatherforecast

import android.content.Context
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import khoina.weatherforecast.data.entity.DayTempEntity
import khoina.weatherforecast.data.entity.ForecastEntity
import khoina.weatherforecast.data.entity.WeatherEntity
import khoina.weatherforecast.data.parseHttpException
import khoina.weatherforecast.data.retrofit.ForecastApi
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.InputStream

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ApiParsingUnitTest {
    lateinit var forecastApi: ForecastApi
    lateinit var mockServer: MockWebServer
    lateinit var gson: Gson

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    val application = ApplicationProvider.getApplicationContext<MainApp>()

    @Before
    fun setupTest() {
        Dispatchers.setMain(mainThreadSurrogate)

        mockServer = MockWebServer()
        mockServer.start()

        gson = application.getAppComponent().gson()

        val retrofit = application.getAppComponent().retrofitBuilder()
            .baseUrl(mockServer.url("/").toString())
            .build()

        forecastApi = retrofit.create(ForecastApi::class.java)
    }

    @After
    fun shutdownTest() {
        mockServer.shutdown()
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    private fun readTextStream(inputStream: InputStream): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString("UTF-8")
    }

    private fun readResponseFromFile(path: String): String {
        val ctx: Context = InstrumentationRegistry.getContext()
        val `is`: InputStream = ctx.getResources().getAssets().open(path)
        val s: String = readTextStream(`is`)
        `is`.close()
        return s
    }

    @Test
    fun apiParsing_isCorrectRequest() = runBlocking<Unit> {
        launch(Dispatchers.Main) {
            mockServer.enqueue(
                MockResponse()
                .setResponseCode(200)
                .setBody(readResponseFromFile("saigon_7days_forecast.json"))
            )

            forecastApi.getDailyForecast("saigon", 7)

            val request = mockServer.takeRequest()
            assertEquals(request.method, "GET")
            assertEquals("/forecast/daily", request.requestUrl?.encodedPath)
            assertEquals(request.requestUrl?.queryParameter("q"), "saigon")
            assertEquals(request.requestUrl?.queryParameter("cnt"), "7")
        }
    }

    @Test
    fun apiParsing_isCorrectResponseListSize() = runBlocking<Unit> {
        launch(Dispatchers.Main) {
            mockServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(readResponseFromFile("saigon_7days_forecast.json"))
            )

            val response = forecastApi.getDailyForecast("saigon", 7)

            assertEquals(response.list.size, 7)
        }
    }

    @Test
    fun apiParsing_isCorrectResponseListItemData() = runBlocking<Unit> {
        launch(Dispatchers.Main) {
            mockServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(readResponseFromFile("newyork_3days_forecast.json"))
            )

            val response = forecastApi.getDailyForecast("new york", 3)

            assertEquals(response.list.size, 3)
            assertEquals(response.list[0], ForecastEntity(
                1584896400, DayTempEntity(4.95f), 1038, 63, listOf(WeatherEntity("overcast clouds"))
            ))
            assertEquals(response.list[1], ForecastEntity(
                1584982800, DayTempEntity(6.13f), 1028, 87, listOf(WeatherEntity("heavy intensity rain"))
            ))
            assertEquals(response.list[2], ForecastEntity(
                1585069200, DayTempEntity(8.17f), 1022, 81, listOf(WeatherEntity("overcast clouds"))
            ))
        }
    }

    @Test
    fun apiParsing_isCorrectErrorParsing() = runBlocking<Unit> {
        launch(Dispatchers.Main) {
            mockServer.enqueue(
                MockResponse()
                    .setResponseCode(404)
                    .setBody(readResponseFromFile("404_city_not_found.json"))
            )

            try {
                forecastApi.getDailyForecast("", 0)
            } catch (ex: HttpException) {
                val parsedHttpException = ex.parseHttpException(gson)
                assertEquals(parsedHttpException.code, "404")
                assertEquals(parsedHttpException.message, "city not found")
            }
        }
    }
}