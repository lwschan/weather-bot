package dev.lewischan.weatherbot.model.openmeteo

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.Instant
import java.time.ZoneId

data class OpenMeteoAirQuality(
    val latitude: Double,
    val longitude: Double,
    val timezone: ZoneId,
    val current: CurrentAirQuality
)

data class CurrentAirQuality(
    val time: Instant,
    val interval: Int,
    val europeanAqi: Int,
    val usAqi: Int,
    @JsonAlias("pm10") val pmTen: Double,
    @JsonAlias("pm2_5") val pmTwoPointFive: Double,
    val carbonMonoxide: Double,
    val nitrogenDioxide: Double,
    val sulphurDioxide: Double,
    val ozone: Double,
    val aerosolOpticalDepth: Double,
    val dust: Double,
    val uvIndex: Double,
    val uvIndexClearSky: Double,
    val ammonia: Double?,
    val alderPollen: Double?,
    val birchPollen: Double?,
    val grassPollen: Double?,
    val mugwortPollen: Double?,
    val olivePollen: Double?
)