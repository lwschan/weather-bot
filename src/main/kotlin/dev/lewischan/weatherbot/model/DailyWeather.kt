package dev.lewischan.weatherbot.model

import java.time.LocalDate
import java.time.ZonedDateTime

data class DailyWeather(
    val date: LocalDate,
    val dailyTemperature: DailyTemperature,
    val dailyFeelsLikeTemperature: DailyTemperature,
    val sunrise: ZonedDateTime,
    val sunset: ZonedDateTime
)
