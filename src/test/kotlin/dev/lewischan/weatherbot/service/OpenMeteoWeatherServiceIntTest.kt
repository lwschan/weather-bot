package dev.lewischan.weatherbot.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.model.Condition
import dev.lewischan.weatherbot.model.Location
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import wiremock.org.eclipse.jetty.http.HttpStatus

class OpenMeteoWeatherServiceIntTest @Autowired constructor(
    private val openMeteoWeatherService: OpenMeteoWeatherService,
    private val wireMockServer: WireMockServer
) : BaseIntTest({

    beforeSpec {
        val testCurrentWeatherResponse = javaClass.getResourceAsStream("/test-current-weather-response.json")
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")!!

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/forecast?latitude=53.990129&longitude=-0.9140249&current=temperature_2m%2Crelative_humidity_2m%2Capparent_temperature%2Cis_day%2Cprecipitation%2Crain%2Cshowers%2Csnowfall%2Cweather_code%2Ccloud_cover%2Cpressure_msl%2Csurface_pressure%2Cwind_speed_10m%2Cwind_direction_10m%2Cwind_gusts_10m&timeformat=unixtime"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody(testCurrentWeatherResponse)
                    .withStatus(HttpStatus.OK_200))
        )
    }

    test("get weather should return the current weather") {
        val currentWeather = openMeteoWeatherService.getWeather(Location("", "", 53.990129, -0.9140249))
        currentWeather shouldNotBe null
        currentWeather!!.temperature.celsius shouldBe 16.6
        currentWeather.condition shouldBe Condition.LIGHT_SHOWERS
    }

})
