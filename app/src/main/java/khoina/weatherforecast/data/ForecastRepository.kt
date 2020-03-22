package khoina.weatherforecast.data

import android.util.Log
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.google.gson.Gson
import khoina.weatherforecast.ForecastModel
import khoina.weatherforecast.data.retrofit.ForecastApi
import khoina.weatherforecast.data.room.ForecastDao
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(
    private val forecastApi: ForecastApi,
    private val forecastDao: ForecastDao,
    private val gson: Gson
) {
    private val entityMapper = ForecastEntityMapper()

    fun getForecastList(place: String, cnt: Int) = liveData<Resource<List<ForecastModel>>>(Dispatchers.IO) {
        val hasFreshData = forecastDao.hasFreshData(place, cnt, System.currentTimeMillis() / 1000 - TIME_OUT_SEC)

        if (hasFreshData) {
            Log.d("khoina", "cache hit")

            emitSource(
                forecastDao.getForecastList(place).map {
                    Resource.Success(it.map(entityMapper::mapModel))
                }
            )
        } else {
            Log.d("khoina", "cache miss")

            try {
                emitSource(
                    forecastDao.getForecastList(place).map {
                        Resource.Loading(it.map(
                            entityMapper::mapModel
                        ))
                    }
                )

                val response = forecastApi.getDailyForecast(place, cnt)
                val records = response.list.map { entityMapper.mapRecord(place, it) }
                forecastDao.replaceAllForecast(place, records)
                Log.d("khoina", "cache records = ${records.size}")

                emitSource(
                    forecastDao.getForecastList(place).map {
                        Resource.Success(it.map(
                            entityMapper::mapModel
                        ))
                    }
                )
            } catch (ex: Exception) {
                emitSource(forecastDao.getForecastList(place).map {
                    Resource.Error<List<ForecastModel>>((ex as? HttpException)?.parseHttpException(gson) ?: ex)
                })
            }
        }
    }

    companion object {
        const val TIME_OUT_SEC = 5
    }
}