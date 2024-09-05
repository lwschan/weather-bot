package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.model.CurrentWeather
import dev.lewischan.weatherbot.model.Location
import dev.lewischan.weatherbot.model.openmeteo.OpenMeteoForecast
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class OpenMeteoWeatherService(
    private val openMeteoRestClient: RestClient
) : WeatherService {

    override fun getWeather(location: Location): CurrentWeather {
        val response:OpenMeteoForecast? = openMeteoRestClient.get()
            .uri("/v1/forecast?latitude=1.340897&longitude=103.8811914&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m&timeformat=unixtime")
            .retrieve()
            .body<OpenMeteoForecast>()

        throw NotImplementedError();
    }

}