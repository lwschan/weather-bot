package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.configuration.PirateWeatherApiProperties
import dev.lewischan.weatherbot.model.Condition
import dev.lewischan.weatherbot.model.CurrentAirQuality
import dev.lewischan.weatherbot.model.CurrentWeather
import dev.lewischan.weatherbot.model.DailyTemperature
import dev.lewischan.weatherbot.model.DailyWeather
import dev.lewischan.weatherbot.model.Humidity
import dev.lewischan.weatherbot.model.Location
import dev.lewischan.weatherbot.model.Temperature
import dev.lewischan.weatherbot.model.pirateweather.PirateWeatherForecast
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.Instant
import java.time.ZoneId
import kotlin.math.roundToInt

@Service
class PirateWeatherWeatherService(
    private val pirateWeatherRestClient: RestClient,
    private val pirateWeatherApiProperties: PirateWeatherApiProperties
) : WeatherService {

    override fun getCurrentWeather(location: Location): CurrentWeather? {
        val response = pirateWeatherRestClient.get()
            .uri(
                "/forecast/{apiKey}/{latitude},{longitude}?units={units}&version={version}&extraVars={extraVars}&include={include}",
                mapOf(
                    "apiKey" to pirateWeatherApiProperties.apiKey,
                    "latitude" to location.latitude,
                    "longitude" to location.longitude,
                    "units" to "si",
                    "version" to 2,
                    "extraVars" to "stationPressure",
                    "include" to "day_night_forecast"
                )
            )
            .retrieve()
            .body<PirateWeatherForecast>()

        return response?.let(::currentWeatherMapper)
    }

    override fun getCurrentAirQuality(location: Location): CurrentAirQuality? {
        throw UnsupportedOperationException("Pirate Weather does not support current air quality")
    }

    fun currentWeatherMapper(forecast: PirateWeatherForecast): CurrentWeather? {
        val timezone = runCatching { ZoneId.of(forecast.timezone) }.getOrNull() ?: return null
        val current = forecast.currently
        val currentTime = Instant.ofEpochSecond(current.time).atZone(timezone)

        val daily = forecast.daily.data
            .firstOrNull { data ->
                Instant.ofEpochSecond(data.time).atZone(timezone).toLocalDate() == currentTime.toLocalDate()
            }
            ?: return null

        return CurrentWeather(
            time = currentTime,
            temperature = Temperature.celsius(current.temperature),
            feelsLikeTemperature = Temperature.celsius(current.apparentTemperature),
            condition = conditionFromIcon(current.icon),
            humidity = Humidity((current.humidity * 100).roundToInt()),
            dailyWeather = DailyWeather(
                date = currentTime.toLocalDate(),
                dailyTemperature = DailyTemperature(
                    low = Temperature.celsius(daily.temperatureMin),
                    high = Temperature.celsius(daily.temperatureMax)
                ),
                dailyFeelsLikeTemperature = DailyTemperature(
                    low = Temperature.celsius(daily.apparentTemperatureMin),
                    high = Temperature.celsius(daily.apparentTemperatureMax)
                ),
                sunrise = Instant.ofEpochSecond(daily.sunriseTime).atZone(timezone),
                sunset = Instant.ofEpochSecond(daily.sunsetTime).atZone(timezone)
            )
        )
    }

    fun conditionFromIcon(icon: String?): Condition = when (icon) {
        "clear-day" -> Condition.SUNNY
        "clear-night" -> Condition.CLEAR
        "partly-cloudy-day", "partly-cloudy-night" -> Condition.PARTLY_CLOUDY
        "cloudy" -> Condition.CLOUDY
        "fog" -> Condition.FOGGY
        "rain" -> Condition.RAIN
        "snow" -> Condition.SNOW
        "sleet" -> Condition.FREEZING_RAIN
        "hail" -> Condition.THUNDERSTORM_WITH_HAIL
        "thunderstorm" -> Condition.THUNDERSTORM
        else -> Condition.UNKNOWN
    }
}
