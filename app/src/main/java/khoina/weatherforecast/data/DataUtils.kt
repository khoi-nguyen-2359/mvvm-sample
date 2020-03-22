package khoina.weatherforecast.data

import com.google.gson.Gson
import retrofit2.HttpException

fun HttpException.parseHttpException(gson: Gson) = ParsedHttpException(this, gson)