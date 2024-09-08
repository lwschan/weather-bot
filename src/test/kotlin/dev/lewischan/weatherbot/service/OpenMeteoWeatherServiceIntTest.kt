package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.model.Location
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertNotNull

class OpenMeteoWeatherServiceIntTest @Autowired constructor(
    private val openMeteoWeatherService: OpenMeteoWeatherService
) : BaseIntTest() {

    @Test
    fun getWeatherShouldReturnCurrentWeather() {
        val currentWeather = openMeteoWeatherService.getWeather(Location(1.340897, 103.8811914))
        assertNotNull(currentWeather)
    }
}