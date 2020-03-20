package khoina.weatherforecast

import android.content.Context
import android.util.Log
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.room.Room
import khoina.weatherforecast.room.ForecastDao
import khoina.weatherforecast.room.ForecastDatabase
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForecastRepository(appContext: Context) {
    private val forecastApi: ForecastApi
    private val entityModel = ForecastEntityMapper()
    private val recordMapper = ForecastRecordMapper()
    private val forecastDao: ForecastDao

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        forecastApi = retrofit.create(ForecastApi::class.java)

        val database = Room.databaseBuilder(
                appContext,
                ForecastDatabase::class.java,
                "forecast_database"
            )
            .fallbackToDestructiveMigration()
            .build()
        forecastDao = database.forecastDao()
    }

    fun getForecastList(place: String, cnt: Int) = liveData<Resource<List<ForecastModel>>>(Dispatchers.IO) {
        val hasFreshData = forecastDao.hasFreshData(place, cnt, System.currentTimeMillis() - TIME_OUT)

        if (hasFreshData) {
            Log.d("ForecastRepository", "cache hit")

            emitSource(
                forecastDao.getForecastList(place).map {
                    Resource.Success(it.map(recordMapper::map))
                }
            )
        } else {
            Log.d("ForecastRepository", "cache miss")

            try {
                emitSource(
                    forecastDao.getForecastList(place).map {
                        Resource.Loading(it.map(recordMapper::map))
                    }
                )

                val response = forecastApi.getDailyForecast(place, cnt)
                val models = response.list.map(entityModel::map)
                val records = models.map { recordMapper.map(place, it) }
                forecastDao.replaceAllForecast(place, records)
                Log.d("ForecastRepository", "cache records = ${records.size}")

                emitSource(
                    forecastDao.getForecastList(place).map {
                        Resource.Success(it.map(recordMapper::map))
                    }
                )
            } catch (ex: Exception) {
                emitSource(forecastDao.getForecastList(place).map {
                    Resource.Error<List<ForecastModel>>(ex)
                })
            }
        }
    }

    companion object {
        const val TIME_OUT = 10000
    }
}