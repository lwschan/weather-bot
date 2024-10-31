package dev.lewischan.weatherbot.model.openmeteo

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

data class OpenMeteoForecast(
    val latitude: Double,
    val longitude: Double,
    val timezone: ZoneId,
    val current: CurrentWeather,
    val daily: DailyWeather
)

data class CurrentWeather(
    val time: Instant,
    val interval: Int,
    @JsonAlias("temperature_2m") val temperatureTwoMetres: Double,
    @JsonAlias("relative_humidity_2m") val relativeHumidityTwoMetres: Int,
    val apparentTemperature: Double,
    val isDay: Boolean,
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
    @JsonAlias("wind_gusts_10m")val windGustTenMetres: Double
)

data class DailyWeather(
    val time: List<Instant>,
    val weatherCode: List<Int>,
    @JsonAlias("temperature_2m_max") val temperatureTwoMetresMax: List<Double>,
    @JsonAlias("temperature_2m_min") val temperatureTwoMetresMin: List<Double>,
    val apparentTemperatureMax: List<Double>,
    val apparentTemperatureMin: List<Double>,
    val sunrise: List<Instant>,
    val sunset: List<Instant>,
    val daylightDuration: List<Duration>,
    val sunshineDuration: List<Duration>,
    val uvIndexMax: List<Double>,
    val uvIndexClearSkyMax: List<Double>,
    val precipitationSum: List<Double>,
    val rainSum: List<Double>,
    val showersSum: List<Double>,
    val snowfallSum: List<Double>,
    val precipitationHours: List<Double>,
    val precipitationProbabilityMax: List<Int>,
    @JsonAlias("wind_speed_10m_max") val windSpeedTenMetresMax: List<Double>,
    @JsonAlias("wind_gusts_10m_max") val windGustsTenMetresMax: List<Double>,
    @JsonAlias("wind_direction_10m_dominant") val windDirectionTenMetresDominant: List<Int>,
    val shortwaveRadiationSum: List<Double>,
    @JsonAlias("et0_fao_evapotranspiration") val referenceEvapotranspiration: List<Double>
)