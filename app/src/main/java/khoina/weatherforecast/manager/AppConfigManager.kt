package khoina.weatherforecast.manager

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfigManager @Inject constructor() {
    private var cacheDurationSec: Long = DEFAULT_DURATION

    fun setCacheDuration(durationSec: Long) {
        this.cacheDurationSec = durationSec
    }

    fun getCacheDuration() = cacheDurationSec

    companion object {
        const val DEFAULT_DURATION = 0L
    }
}