package dev.lewischan.weatherbot.core.weather.model

import java.time.LocalDate
import java.time.ZonedDateTime

data class DailyWeather(
    val date: LocalDate,
    val condition: Condition,
    val precipitationProbability: Int,
    val dailyTemperature: DailyTemperature,
    val dailyFeelsLikeTemperature: DailyTemperature,
    val sunrise: ZonedDateTime,
    val sunset: ZonedDateTime
)
