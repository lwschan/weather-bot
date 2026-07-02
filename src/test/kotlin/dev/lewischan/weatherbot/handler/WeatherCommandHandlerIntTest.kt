package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import dev.lewischan.weatherbot.BaseIntTest
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

    test("both weather commands should be registered") {
        commandHandlers.map { it.command } shouldContain "w"
        commandHandlers.map { it.command } shouldContain "wp"
        defaultWeatherCommandHandler.command shouldBe "w"
        pirateWeatherWeatherCommandHandler.command shouldBe "wp"
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
                    it shouldNotContain "\n "
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
                    it shouldNotContain "\n "
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
                replyToMessageId = 1L
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
