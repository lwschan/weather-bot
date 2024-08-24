package dev.lewischan.weatherbot.model.openmeteo

import com.fasterxml.jackson.annotation.JsonAlias

data class OpenMeteoForecast(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeather
)

data class CurrentWeather(
    val time: Long,
    val interval: Int,
    @JsonAlias("temperature_2m") val temperatureTwoMetres: Double,
    @JsonAlias("relative_humidity_2m") val relativeHumidityTwoMetres: Int,
    val apparentTemperature: Double,
    val isDay: Int,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    val weatherCode: Int,
    val cloudCover: Int,
    val pressureMsl: Double,
    val surfacePressure: Double,
    @JsonAlias("wind_speed_10m") val windSpeedTenMetres: Double,
    @JsonAlias("wind_direction_10m")val windDirectionTenMetres: Int,
    @JsonAlias("wind_gust_10m")val windGustTenMetres: Double
)
