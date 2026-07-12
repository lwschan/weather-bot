package dev.lewischan.weatherbot.providers.openmeteo.service

import dev.lewischan.weatherbot.core.model.Condition
import dev.lewischan.weatherbot.core.model.CurrentAirQuality
import dev.lewischan.weatherbot.core.model.CurrentWeather
import dev.lewischan.weatherbot.core.model.DailyTemperature
import dev.lewischan.weatherbot.core.model.DailyWeather
import dev.lewischan.weatherbot.core.model.Humidity
import dev.lewischan.weatherbot.locations.Location
import dev.lewischan.weatherbot.core.model.Temperature
import dev.lewischan.weatherbot.core.service.WeatherService
import dev.lewischan.weatherbot.providers.openmeteo.model.OpenMeteoAirQuality
import dev.lewischan.weatherbot.providers.openmeteo.model.OpenMeteoForecast
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.LocalDate
import java.time.ZonedDateTime

@Service
@Primary
class OpenMeteoWeatherService(
    private val openMeteoWeatherRestClient: RestClient,
    private val openMeteoAirQualityRestClient: RestClient
) : WeatherService {

    override fun getCurrentWeather(location: Location): CurrentWeather? {
        return getForecast(location, 1)?.let(::currentWeatherMapper)
    }

    override fun getDailyForecast(location: Location, daysAhead: Int): DailyWeather? {
        require(daysAhead >= 0) { "daysAhead must not be negative" }
        if (daysAhead > MAX_FORECAST_DAYS_AHEAD) return null

        val forecast = getForecast(location, daysAhead + 1) ?: return null
        val targetDate = forecast.current.time.atZone(forecast.timezone).toLocalDate().plusDays(daysAhead.toLong())
        return dailyWeatherMapper(forecast, targetDate)
    }

    private fun getForecast(location: Location, forecastDays: Int): OpenMeteoForecast? {
        return openMeteoWeatherRestClient.get()
            .uri("/v1/forecast?latitude={latitude}&longitude={longitude}&current={current}&daily={daily}&timeformat={timeFormat}&timezone={timezone}&forecast_days={forecastDays}", mapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "current" to "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m",
                "daily" to "weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,daylight_duration,sunshine_duration,uv_index_max,uv_index_clear_sky_max,precipitation_sum,rain_sum,showers_sum,snowfall_sum,precipitation_hours,precipitation_probability_max,wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant,shortwave_radiation_sum,et0_fao_evapotranspiration",
                "timeFormat" to "unixtime",
                "timezone" to "auto",
                "forecastDays" to forecastDays
            ))
            .retrieve()
            .body<OpenMeteoForecast>()
    }

    override fun getCurrentAirQuality(location: Location): CurrentAirQuality? {
        val response = openMeteoAirQualityRestClient.get()
            .uri("/v1/air-quality?latitude={latitude}&longitude={longitude}&current={current}&timeformat={timeFormat}&timezone={timezone}", mapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "current" to "european_aqi,us_aqi,pm10,pm2_5,carbon_monoxide,nitrogen_dioxide,sulphur_dioxide,ozone,aerosol_optical_depth,dust,uv_index,uv_index_clear_sky,ammonia,alder_pollen,birch_pollen,grass_pollen,mugwort_pollen,olive_pollen",
                "timeFormat" to "unixtime",
                "timezone" to "auto"
            ))
            .retrieve()
            .body<OpenMeteoAirQuality>()

        return response?.let { currentAirQualityMapper(it) }
    }

    fun currentWeatherMapper(openMeteoForecast: OpenMeteoForecast): CurrentWeather? {
        val today = ZonedDateTime.ofInstant(openMeteoForecast.current.time, openMeteoForecast.timezone)
        val dailyWeather = dailyWeatherMapper(openMeteoForecast, today.toLocalDate()) ?: return null

        return CurrentWeather(
            time = today,
            temperature = Temperature.celsius(openMeteoForecast.current.temperatureTwoMetres),
            feelsLikeTemperature = Temperature.celsius(openMeteoForecast.current.apparentTemperature),
            condition = Condition.fromWmoCodeAndIsDay(openMeteoForecast.current.weatherCode, openMeteoForecast.current.isDay),
            humidity = Humidity(openMeteoForecast.current.relativeHumidityTwoMetres),
            dailyWeather = dailyWeather
        )
    }

    private fun dailyWeatherMapper(openMeteoForecast: OpenMeteoForecast, date: LocalDate): DailyWeather? {
        val daily = openMeteoForecast.daily
        val index = daily.time.indexOfFirst { it.atZone(openMeteoForecast.timezone).toLocalDate() == date }
        if (index < 0) return null

        return DailyWeather(
            date = date,
            condition = Condition.fromWmoCodeAndIsDay(daily.weatherCode[index], true),
            precipitationProbability = daily.precipitationProbabilityMax[index],
            dailyTemperature = DailyTemperature(
                low = Temperature.celsius(daily.temperatureTwoMetresMin[index]),
                high = Temperature.celsius(daily.temperatureTwoMetresMax[index])
            ),
            dailyFeelsLikeTemperature = DailyTemperature(
                low = Temperature.celsius(daily.apparentTemperatureMin[index]),
                high = Temperature.celsius(daily.apparentTemperatureMax[index])
            ),
            sunrise = ZonedDateTime.ofInstant(daily.sunrise[index], openMeteoForecast.timezone),
            sunset = ZonedDateTime.ofInstant(daily.sunset[index], openMeteoForecast.timezone)
        )
    }

    fun currentAirQualityMapper(openMeteoAirQuality: OpenMeteoAirQuality): CurrentAirQuality {
        val today = ZonedDateTime.ofInstant(openMeteoAirQuality.current.time, openMeteoAirQuality.timezone)

        return CurrentAirQuality(
            today,
            openMeteoAirQuality.current.europeanAqi,
            openMeteoAirQuality.current.usAqi,
            openMeteoAirQuality.current.uvIndex,
            openMeteoAirQuality.current.uvIndexClearSky,
            openMeteoAirQuality.current.pmTwoPointFive,
            openMeteoAirQuality.current.pmTen,
        )
    }

    companion object {
        private const val MAX_FORECAST_DAYS_AHEAD = 15
    }

}
