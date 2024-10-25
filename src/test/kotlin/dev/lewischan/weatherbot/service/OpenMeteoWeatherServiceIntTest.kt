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
import java.time.ZonedDateTime

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
            get(urlEqualTo("/v1/forecast?latitude=1.3602148&longitude=103.9871849&current=temperature_2m%2Crelative_humidity_2m%2Capparent_temperature%2Cis_day%2Cprecipitation%2Crain%2Cshowers%2Csnowfall%2Cweather_code%2Ccloud_cover%2Cpressure_msl%2Csurface_pressure%2Cwind_speed_10m%2Cwind_direction_10m%2Cwind_gusts_10m&timeformat=unixtime&timezone=auto"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody(testCurrentWeatherResponse)
                    .withStatus(HttpStatus.OK_200))
        )
    }

    test("get weather should return the current weather") {
        val currentWeather = openMeteoWeatherService.getCurrentWeather(Location("", "", 1.3602148, 103.9871849))
        currentWeather shouldNotBe null
        currentWeather!!.temperature.celsius shouldBe 27.4
        currentWeather.condition shouldBe Condition.MAINLY_CLEAR
        currentWeather.time shouldBe ZonedDateTime.parse("2024-10-23T01:45+08:00[Asia/Singapore]")
    }

})
