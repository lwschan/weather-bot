package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.model.CurrentWeather
import dev.lewischan.weatherbot.model.Location
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class OpenMeteoWeatherService(
    private val openMeteoRestClient: RestClient
) : WeatherService {

    override fun getWeather(location: Location): CurrentWeather {
        val response:String? = openMeteoRestClient.get()
            .uri("")
            .retrieve()
            .body<String>()

        throw NotImplementedError();
    }

}