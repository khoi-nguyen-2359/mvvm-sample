package khoina.weatherforecast.data.repo

import androidx.lifecycle.LiveData
import khoina.weatherforecast.ForecastModel
import khoina.weatherforecast.data.Resource

interface ForecastRepository {
    fun getForecastList(place: String, count: Int): LiveData<Resource<List<ForecastModel>>>
}