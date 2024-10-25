package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.model.Condition
import dev.lewischan.weatherbot.model.CurrentWeather
import dev.lewischan.weatherbot.model.Location
import dev.lewischan.weatherbot.model.Temperature
import dev.lewischan.weatherbot.model.openmeteo.OpenMeteoForecast
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.ZonedDateTime

@Service
class OpenMeteoWeatherService(
    private val openMeteoRestClient: RestClient
) : WeatherService {

    override fun getCurrentWeather(location: Location): CurrentWeather? {
        val response:OpenMeteoForecast? = openMeteoRestClient.get()
            .uri("/v1/forecast?latitude={latitude}&longitude={longitude}&current={current}&timeformat={timeFormat}&timezone={timezone}", mapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "current" to "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m",
                "timeFormat" to "unixtime",
                "timezone" to "auto"
            ))
            .retrieve()
            .body<OpenMeteoForecast>()

        return response?.let { currentWeatherMapper(it) }
    }

    fun currentWeatherMapper(openMeteoForecast: OpenMeteoForecast): CurrentWeather {
        return CurrentWeather(
            time = ZonedDateTime.ofInstant(openMeteoForecast.current.time, openMeteoForecast.timezone),
            temperature = Temperature.celsius(openMeteoForecast.current.temperatureTwoMetres),
            feelsLikeTemperature = Temperature.celsius(openMeteoForecast.current.apparentTemperature),
            condition = Condition.fromWmoCodeAndIsDay(openMeteoForecast.current.weatherCode, openMeteoForecast.current.isDay)
        )
    }

}