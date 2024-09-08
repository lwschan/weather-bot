package dev.lewischan.weatherbot.model

import java.time.Instant

data class CurrentWeather(
    val time: Instant,
    val temperature: Temperature,
    val feelsLikeTemperature: Temperature,
    val condition: Condition,
)
