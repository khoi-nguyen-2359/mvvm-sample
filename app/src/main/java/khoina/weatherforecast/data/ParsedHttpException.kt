package khoina.weatherforecast.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import khoina.weatherforecast.data.entity.ErrorResponseEntity
import retrofit2.HttpException

class ParsedHttpException(
    httpException: HttpException,
    gson: Gson,
    val errorResponse: ErrorResponseEntity = httpException.response()?.errorBody()?.string()?.let {
        try {
            gson.fromJson(it, ErrorResponseEntity::class.java)
        } catch (ex: JsonSyntaxException) {
            null
        }
    } ?: ErrorResponseEntity(httpException.code().toString(), httpException.message())
) : Exception(errorResponse.message) {
    val code: String = errorResponse.cod
}