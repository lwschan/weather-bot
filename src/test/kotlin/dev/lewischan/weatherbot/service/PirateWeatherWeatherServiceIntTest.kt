package dev.lewischan.weatherbot.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.model.Condition
import dev.lewischan.weatherbot.model.Location
import dev.lewischan.weatherbot.model.pirateweather.PirateWeatherForecast
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import wiremock.org.eclipse.jetty.http.HttpStatus
import java.time.ZonedDateTime

class PirateWeatherWeatherServiceIntTest(
    private val pirateWeatherWeatherService: PirateWeatherWeatherService,
    @Qualifier("pirateWeatherRestClient") private val pirateWeatherRestClient: RestClient,
    private val wireMockServer: WireMockServer
) : BaseIntTest({

    lateinit var testResponse: String

    beforeSpec {
        testResponse = javaClass.getResourceAsStream("/test-pirate-weather-response.json")
            ?.bufferedReader()
            ?.readText()!!

        wireMockServer.stubFor(
            get(urlPathEqualTo("/forecast/test-pirate-weather-api-key/1.3602148,103.9871849"))
                .withQueryParam("units", equalTo("si"))
                .withQueryParam("version", equalTo("2"))
                .withQueryParam("extraVars", equalTo("stationPressure"))
                .withQueryParam("include", equalTo("day_night_forecast"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(testResponse)
                        .withStatus(HttpStatus.OK_200)
                )
        )

        wireMockServer.stubFor(
            get(urlPathEqualTo("/pirate-weather-full-response"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(testResponse)
                        .withStatus(HttpStatus.OK_200)
                )
        )
    }

    test("get weather should return mapped current and daily weather") {
        val currentWeather = pirateWeatherWeatherService.getCurrentWeather(
            Location("", 1.3602148, 103.9871849)
        )

        currentWeather shouldNotBe null
        currentWeather!!.time shouldBe ZonedDateTime.parse("2024-10-31T16:00+08:00[Asia/Singapore]")
        currentWeather.temperature.celsius shouldBe 31.25
        currentWeather.feelsLikeTemperature.celsius shouldBe 34.5
        currentWeather.humidity.value shouldBe 73
        currentWeather.condition shouldBe Condition.PARTLY_CLOUDY
        currentWeather.dailyWeather.dailyTemperature.low.celsius shouldBe 24.75
        currentWeather.dailyWeather.dailyTemperature.high.celsius shouldBe 32.5
        currentWeather.dailyWeather.dailyFeelsLikeTemperature.low.celsius shouldBe 26.25
        currentWeather.dailyWeather.dailyFeelsLikeTemperature.high.celsius shouldBe 36.75
        currentWeather.dailyWeather.sunrise shouldBe ZonedDateTime.parse("2024-10-31T06:00+08:00[Asia/Singapore]")
        currentWeather.dailyWeather.sunset shouldBe ZonedDateTime.parse("2024-10-31T18:00+08:00[Asia/Singapore]")
    }

    test("full response should deserialize all forecast blocks") {
        val forecast = pirateWeatherRestClient.get()
            .uri("/pirate-weather-full-response")
            .retrieve()
            .body<PirateWeatherForecast>()!!

        forecast.minutely.data.first().rainIntensity shouldBe 0.0
        forecast.hourly.data.first().stationPressure shouldBe 1009.92
        forecast.dayNight.data.first().solar shouldBe 319.73
        forecast.alerts.first().regions shouldBe listOf("Singapore")
        forecast.flags.sourceTimes.hrrrZeroToEighteen shouldBe "2024-10-31 06Z"
        forecast.flags.sourceIdx.dwdMosmix?.stations?.first()?.name shouldBe "SINGAPORE"
    }

    test("get air quality should report that the operation is unsupported") {
        shouldThrow<UnsupportedOperationException> {
            pirateWeatherWeatherService.getCurrentAirQuality(Location("", 1.3602148, 103.9871849))
        }
    }

    test("condition mapper should map documented weather icons") {
        mapOf(
            "clear-day" to Condition.SUNNY,
            "clear-night" to Condition.CLEAR,
            "partly-cloudy-day" to Condition.PARTLY_CLOUDY,
            "partly-cloudy-night" to Condition.PARTLY_CLOUDY,
            "cloudy" to Condition.CLOUDY,
            "fog" to Condition.FOGGY,
            "rain" to Condition.RAIN,
            "snow" to Condition.SNOW,
            "sleet" to Condition.FREEZING_RAIN,
            "hail" to Condition.THUNDERSTORM_WITH_HAIL,
            "thunderstorm" to Condition.THUNDERSTORM,
            "wind" to Condition.UNKNOWN,
            "tornado" to Condition.UNKNOWN
        ).forEach { (icon, condition) ->
            pirateWeatherWeatherService.conditionFromIcon(icon) shouldBe condition
        }
    }
})
