package dev.lewischan.weatherbot.model

import java.time.ZonedDateTime

data class CurrentWeather(
    val time: ZonedDateTime,
    val temperature: Temperature,
    val feelsLikeTemperature: Temperature,
    val condition: Condition,
    val humidity: Humidity,
    val dailyWeather: DailyWeather
)
