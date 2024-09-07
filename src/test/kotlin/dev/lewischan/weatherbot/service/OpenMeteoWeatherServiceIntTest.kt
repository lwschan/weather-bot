package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.model.Location
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class OpenMeteoWeatherServiceIntTest @Autowired constructor(
    private val openMeteoWeatherService: OpenMeteoWeatherService
) : BaseIntTest() {

    @Test
    fun getWeatherShouldReturnCurrentWeather() {
        openMeteoWeatherService.getWeather(Location("1.1", "1.2"))
    }

}