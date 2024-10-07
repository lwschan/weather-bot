package dev.lewischan.weatherbot.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.model.Condition
import dev.lewischan.weatherbot.model.Location
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import wiremock.org.eclipse.jetty.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OpenMeteoWeatherServiceIntTest @Autowired constructor(
    private val openMeteoWeatherService: OpenMeteoWeatherService,
    private val wireMockServer: WireMockServer
) : BaseIntTest() {

    private var testCurrentWeatherResponse: String = ""

    @BeforeAll
    fun setup() {
        testCurrentWeatherResponse = javaClass.getResourceAsStream("/test-current-weather-response.json")
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")!!

        wireMockServer.start()
        wireMockServer.stubFor(
            get(urlEqualTo("/v1/forecast?latitude=53.990129&longitude=-0.9140249&current=temperature_2m%2Crelative_humidity_2m%2Capparent_temperature%2Cis_day%2Cprecipitation%2Crain%2Cshowers%2Csnowfall%2Cweather_code%2Ccloud_cover%2Cpressure_msl%2Csurface_pressure%2Cwind_speed_10m%2Cwind_direction_10m%2Cwind_gusts_10m&timeformat=unixtime"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody(testCurrentWeatherResponse)
                    .withStatus(HttpStatus.OK_200))
        )
    }

    @AfterAll
    fun cleanup() {
        wireMockServer.stop()
    }

    @Test
    fun getWeatherShouldReturnCurrentWeather() {
        val currentWeather = openMeteoWeatherService.getWeather(Location("", "", 53.990129, -0.9140249))
        assertNotNull(currentWeather)
        assertEquals(16.6, currentWeather.temperature.celsius)
        assertEquals(Condition.LIGHT_SHOWERS, currentWeather.condition)
    }
}