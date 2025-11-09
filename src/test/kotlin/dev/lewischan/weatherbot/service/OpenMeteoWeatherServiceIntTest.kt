package dev.lewischan.weatherbot.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import dev.lewischan.weatherbot.UseBaseIntTest
import dev.lewischan.weatherbot.model.Condition
import dev.lewischan.weatherbot.model.Location
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import wiremock.org.eclipse.jetty.http.HttpStatus
import java.time.ZonedDateTime

@UseBaseIntTest
class OpenMeteoWeatherServiceIntTest(
    private val openMeteoWeatherService: OpenMeteoWeatherService,
    private val wireMockServer: WireMockServer
) : FunSpec({

    beforeSpec {
        val testCurrentWeatherResponse = javaClass.getResourceAsStream("/test-current-weather-response.json")
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")!!

        wireMockServer.stubFor(
            get(urlEqualTo("/v1/forecast" +
                    "?latitude=1.3602148" +
                    "&longitude=103.9871849" +
                    "&current=temperature_2m%2Crelative_humidity_2m%2Capparent_temperature%2Cis_day%2Cprecipitation%2Crain%2Cshowers%2Csnowfall%2Cweather_code%2Ccloud_cover%2Cpressure_msl%2Csurface_pressure%2Cwind_speed_10m%2Cwind_direction_10m%2Cwind_gusts_10m" +
                    "&daily=weather_code%2Ctemperature_2m_max%2Ctemperature_2m_min%2Capparent_temperature_max%2Capparent_temperature_min%2Csunrise%2Csunset%2Cdaylight_duration%2Csunshine_duration%2Cuv_index_max%2Cuv_index_clear_sky_max%2Cprecipitation_sum%2Crain_sum%2Cshowers_sum%2Csnowfall_sum%2Cprecipitation_hours%2Cprecipitation_probability_max%2Cwind_speed_10m_max%2Cwind_gusts_10m_max%2Cwind_direction_10m_dominant%2Cshortwave_radiation_sum%2Cet0_fao_evapotranspiration" +
                    "&timeformat=unixtime" +
                    "&timezone=auto"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody(testCurrentWeatherResponse)
                    .withStatus(HttpStatus.OK_200))
        )
    }

    test("get weather should return the current weather") {
        val currentWeather = openMeteoWeatherService.getCurrentWeather(Location("", 1.3602148, 103.9871849))
        currentWeather shouldNotBe null
        currentWeather!!.temperature.celsius shouldBe 31.0
        currentWeather.condition shouldBe Condition.LIGHT_SHOWERS
        currentWeather.time shouldBe ZonedDateTime.parse("2024-10-31T16:00+08:00[Asia/Singapore]")
    }

})
