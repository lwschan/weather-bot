package dev.lewischan.weatherbot.model

import java.time.Instant

data class CurrentWeather(
    val time: Instant,
    val temperature: Temperature,
    val condition: String,
)
