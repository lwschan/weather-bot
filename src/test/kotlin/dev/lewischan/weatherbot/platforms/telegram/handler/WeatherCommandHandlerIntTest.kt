package dev.lewischan.weatherbot.platforms.telegram.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyParameters
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import dev.lewischan.weatherbot.core.test.BaseIntTest
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import wiremock.org.eclipse.jetty.http.HttpStatus
import java.security.SecureRandom

class WeatherCommandHandlerIntTest(
    private val defaultWeatherCommandHandler: DefaultWeatherCommandHandler,
    private val pirateWeatherWeatherCommandHandler: PirateWeatherWeatherCommandHandler,
    private val tomorrowWeatherCommandHandler: TomorrowWeatherCommandHandler,
    private val pirateWeatherTomorrowCommandHandler: PirateWeatherTomorrowCommandHandler,
    private val commandHandlers: List<CommandHandler>,
    private val wireMockServer: WireMockServer,
    private val bot: Bot
) : BaseIntTest({

    val random = SecureRandom()

    beforeSpec {
        fun resourceBody(path: String): String = javaClass.getResourceAsStream(path)
            ?.bufferedReader()
            ?.readText()!!

        wireMockServer.stubFor(
            get(urlPathEqualTo("/forecast/test-pirate-weather-api-key/53.990129,-0.9140249"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceBody("/test-pirate-weather-response.json"))
                        .withStatus(HttpStatus.OK_200)
                )
        )
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v1/forecast"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceBody("/test-current-weather-response.json"))
                        .withStatus(HttpStatus.OK_200)
                )
        )
        wireMockServer.stubFor(
            get(urlPathEqualTo("/v1/air-quality"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceBody("/test-current-air-quality-response.json"))
                        .withStatus(HttpStatus.OK_200)
                )
        )
    }

    beforeEach {
        wireMockServer.resetRequests()
        every { bot.getMe().get().username } returns "test_bot"
    }

    afterEach {
        clearMocks(bot)
    }

    test("all weather commands should be registered") {
        commandHandlers.map { it.command } shouldContain "w"
        commandHandlers.map { it.command } shouldContain "wp"
        commandHandlers.map { it.command } shouldContain "wt"
        commandHandlers.map { it.command } shouldContain "wpt"
        defaultWeatherCommandHandler.command shouldBe "w"
        pirateWeatherWeatherCommandHandler.command shouldBe "wp"
        tomorrowWeatherCommandHandler.command shouldBe "wt"
        pirateWeatherTomorrowCommandHandler.command shouldBe "wpt"
    }

    test("default weather command should use OpenMeteo and include air quality") {
        val chatId = random.nextLong()
        val message = addressMessage(chatId, "/w Stamford Bridge, London")

        defaultWeatherCommandHandler.execute(message)

        verify(exactly = 1) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = match {
                    it shouldContain "Stamford Bridge, York YO41, UK"
                    it shouldContain "<b>AQI (US / EU):</b> 31 / 21"
                    true
                },
                parseMode = ParseMode.HTML
            )
        }
        wireMockServer.verify(getRequestedFor(urlPathEqualTo("/v1/forecast")))
        wireMockServer.verify(getRequestedFor(urlPathEqualTo("/v1/air-quality")))
    }

    test("Pirate Weather command should omit all air quality output") {
        val chatId = random.nextLong()
        val message = addressMessage(chatId, "/wp Stamford Bridge, London")

        pirateWeatherWeatherCommandHandler.execute(message)

        verify(exactly = 1) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = match {
                    it shouldContain "Stamford Bridge, York YO41, UK"
                    it shouldNotContain "AQI"
                    it shouldNotContain "PM 2.5"
                    it shouldNotContain "No air quality data available"
                    true
                },
                parseMode = ParseMode.HTML
            )
        }
        wireMockServer.verify(
            getRequestedFor(
                urlPathEqualTo("/forecast/test-pirate-weather-api-key/53.990129,-0.9140249")
            )
        )
        wireMockServer.verify(0, getRequestedFor(urlPathEqualTo("/v1/air-quality")))
    }

    test("tomorrow weather command should use OpenMeteo and omit air quality") {
        val chatId = random.nextLong()
        val message = addressMessage(chatId, "/wt Stamford Bridge, London")

        tomorrowWeatherCommandHandler.execute(message)

        verify(exactly = 1) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = match {
                    it shouldContain "<b>Tomorrow · Fri, 01 Nov</b>"
                    it shouldContain "<b>Rain:</b> 78%"
                    it shouldContain "<b>H:</b> 32.9°C"
                    it shouldNotContain "AQI"
                    true
                },
                parseMode = ParseMode.HTML
            )
        }
        wireMockServer.verify(getRequestedFor(urlPathEqualTo("/v1/forecast")))
        wireMockServer.verify(0, getRequestedFor(urlPathEqualTo("/v1/air-quality")))
    }

    test("Pirate Weather tomorrow command should return tomorrow forecast") {
        val chatId = random.nextLong()
        val message = addressMessage(chatId, "/wpt Stamford Bridge, London")

        pirateWeatherTomorrowCommandHandler.execute(message)

        verify(exactly = 1) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = match {
                    it shouldContain "<b>Tomorrow · Fri, 01 Nov</b>"
                    it shouldContain "<code>🌧️ Rain</code>"
                    it shouldContain "<b>Rain:</b> 80%"
                    it shouldContain "<b>H:</b> 30.0°C"
                    true
                },
                parseMode = ParseMode.HTML
            )
        }
        wireMockServer.verify(
            getRequestedFor(
                urlPathEqualTo("/forecast/test-pirate-weather-api-key/53.990129,-0.9140249")
            )
        )
        wireMockServer.verify(0, getRequestedFor(urlPathEqualTo("/v1/air-quality")))
    }

    test("weather commands should preserve invalid address handling") {
        val chatId = random.nextLong()
        val message = addressMessage(chatId, "/wp Random Address")

        pirateWeatherWeatherCommandHandler.execute(message)
        message.text shouldBe "/wp Random Address"

        verify(exactly = 1) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = "Could not find a valid address for Random Address.",
                replyParameters = ReplyParameters(messageId = 1L)
            )
        }
    }
}) {
    companion object {
        fun addressMessage(chatId: Long, text: String): Message {
            val message = mockk<Message>()
            every { message.chat.id } returns chatId
            every { message.text } returns text
            every { message.messageId } returns 1L
            return message
        }
    }
}
