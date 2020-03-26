package khoina.weatherforecast.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.google.gson.Gson
import khoina.weatherforecast.ForecastModel
import khoina.weatherforecast.data.ForecastEntityMapper
import khoina.weatherforecast.data.Resource
import khoina.weatherforecast.data.parseHttpException
import khoina.weatherforecast.data.retrofit.ForecastApi
import khoina.weatherforecast.data.room.ForecastDao
import khoina.weatherforecast.manager.AppConfigManager
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

class ForecastRepositoryImpl @Inject constructor(
    private val forecastApi: ForecastApi,
    private val forecastDao: ForecastDao,
    private val gson: Gson,
    private val appConfigManager: AppConfigManager
): ForecastRepository {
    private val entityMapper = ForecastEntityMapper()

    override fun getForecastList(place: String, count: Int): LiveData<Resource<List<ForecastModel>>> = liveData<Resource<List<ForecastModel>>>(Dispatchers.IO) {
        val hasFreshData = appConfigManager.getCacheDuration() != 0L
                && forecastDao.hasFreshData(place, count, System.currentTimeMillis() / 1000 - appConfigManager.getCacheDuration())

        if (hasFreshData) {
            Log.d("khoina", "cache hit")

            emitSource(
                forecastDao.getForecastList(place).map {
                    Resource.Success(
                        it.map(
                            entityMapper::mapModel
                        )
                    )
                }
            )
        } else {
            Log.d("khoina", "cache miss")

            try {
                val disposable = emitSource(
                    forecastDao.getForecastList(place).map {
                        Resource.Loading(
                            it.map(
                                entityMapper::mapModel
                            )
                        )
                    }
                )
                val response = forecastApi.getDailyForecast(place, count)
                val records = response.list.map { entityMapper.mapRecord(place, it) }
                disposable.dispose()
                forecastDao.replaceAllForecast(place, records)
                Log.d("khoina", "cache records = ${records.size}")

                emitSource(
                    forecastDao.getForecastList(place).map {
                        Resource.Success(
                            it.map(
                                entityMapper::mapModel
                            )
                        )
                    }
                )
            } catch (ex: Exception) {
                emitSource(forecastDao.getForecastList(place).map {
                    Resource.Error<List<ForecastModel>>(
                        (ex as? HttpException)?.parseHttpException(gson) ?: ex
                    )
                })
            }
        }
    }
}