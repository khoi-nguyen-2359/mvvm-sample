package khoina.weatherforecast

sealed class Resource<T>(val data: T?) {
   class Success<T>(data: T) : Resource<T>(data)
   class Error<T>(val exception: Exception, data: T? = null) : Resource<T>(data)
   class Loading<T>(data: T? = null) : Resource<T>(data)
}