package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.model.Condition
import dev.lewischan.weatherbot.model.CurrentAirQuality
import dev.lewischan.weatherbot.model.CurrentWeather
import dev.lewischan.weatherbot.model.DailyTemperature
import dev.lewischan.weatherbot.model.DailyWeather
import dev.lewischan.weatherbot.model.Humidity
import dev.lewischan.weatherbot.model.Location
import dev.lewischan.weatherbot.model.Temperature
import dev.lewischan.weatherbot.model.openmeteo.OpenMeteoAirQuality
import dev.lewischan.weatherbot.model.openmeteo.OpenMeteoForecast
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.ZonedDateTime

@Service
class OpenMeteoWeatherService(
    private val openMeteoWeatherRestClient: RestClient,
    private val openMeteoAirQualityRestClient: RestClient
) : WeatherService {

    override fun getCurrentWeather(location: Location): CurrentWeather? {
        val response:OpenMeteoForecast? = openMeteoWeatherRestClient.get()
            .uri("/v1/forecast?latitude={latitude}&longitude={longitude}&current={current}&daily={daily}&timeformat={timeFormat}&timezone={timezone}", mapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "current" to "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m",
                "daily" to "weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,daylight_duration,sunshine_duration,uv_index_max,uv_index_clear_sky_max,precipitation_sum,rain_sum,showers_sum,snowfall_sum,precipitation_hours,precipitation_probability_max,wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant,shortwave_radiation_sum,et0_fao_evapotranspiration",
                "timeFormat" to "unixtime",
                "timezone" to "auto"
            ))
            .retrieve()
            .body<OpenMeteoForecast>()

        return response?.let { currentWeatherMapper(it) }
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

    fun currentWeatherMapper(openMeteoForecast: OpenMeteoForecast): CurrentWeather {
        val today = ZonedDateTime.ofInstant(openMeteoForecast.current.time, openMeteoForecast.timezone)
        val todayIndex = openMeteoForecast.daily.time.indexOfFirst { instant ->
            ZonedDateTime.ofInstant(instant, openMeteoForecast.timezone).toLocalDate() == today.toLocalDate()
        }
        val todayHigh = openMeteoForecast.daily.temperatureTwoMetresMax[todayIndex]
        val todayLow = openMeteoForecast.daily.temperatureTwoMetresMin[todayIndex]
        val todayFeelsLikeHigh = openMeteoForecast.daily.apparentTemperatureMax[todayIndex]
        val todayFeelsLikeLow = openMeteoForecast.daily.apparentTemperatureMin[todayIndex]

        return CurrentWeather(
            time = today,
            temperature = Temperature.celsius(openMeteoForecast.current.temperatureTwoMetres),
            feelsLikeTemperature = Temperature.celsius(openMeteoForecast.current.apparentTemperature),
            condition = Condition.fromWmoCodeAndIsDay(openMeteoForecast.current.weatherCode, openMeteoForecast.current.isDay),
            humidity = Humidity(openMeteoForecast.current.relativeHumidityTwoMetres),
            dailyWeather = DailyWeather(
                date = today.toLocalDate(),
                dailyTemperature = DailyTemperature(
                    low = Temperature.celsius(todayLow),
                    high = Temperature.celsius(todayHigh)
                ),
                dailyFeelsLikeTemperature = DailyTemperature(
                    low = Temperature.celsius(todayFeelsLikeLow),
                    high = Temperature.celsius(todayFeelsLikeHigh)
                ),
                sunrise = ZonedDateTime.ofInstant(openMeteoForecast.daily.sunrise[todayIndex], openMeteoForecast.timezone),
                sunset = ZonedDateTime.ofInstant(openMeteoForecast.daily.sunset[todayIndex], openMeteoForecast.timezone)
            )
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

}
