package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.model.Location
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.Test

class OpenMeteoWeatherServiceIntTest @Autowired constructor(
    private val openMeteoWeatherService: OpenMeteoWeatherService
) : BaseIntTest() {

    @Test
    fun getWeatherShouldReturnCurrentWeather() {
        openMeteoWeatherService.getWeather(Location("", ""))
    }

}