package dev.lewischan.weatherbot.model

import java.time.LocalDate

data class DailyWeather(
    val date: LocalDate,
    val dailyTemperature: DailyTemperature,
    val dailyFeelsLikeTemperature: DailyTemperature
)
