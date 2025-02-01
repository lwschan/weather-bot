package dev.lewischan.weatherbot.model

import java.time.ZonedDateTime

data class CurrentAirQuality(
    val time: ZonedDateTime,
    val europeanAqi: Int,
    val usAqi: Int,
    val uvIndex: Double,
    val uvIndexClearSky: Double,
    val pmTwoPointFive: Double,
    val pmTen: Double,
)
