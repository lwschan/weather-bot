package dev.lewischan.weatherbot.controller

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import io.kotest.assertions.throwables.shouldNotThrowAny
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

class TelegramBotControllerIntTest(
    private val webTestClient: WebTestClient,
    private val telegramBotProperties: TelegramBotProperties
) : BaseIntTest({

    test("telegram-bot endpoint should accept the request if token is valid") {
        shouldNotThrowAny {
            webTestClient.post().uri("/telegram-bot/${telegramBotProperties.apiToken}")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
        }
    }

    test("telegram-bot endpoint should reject the request if token is invalid") {
        shouldNotThrowAny {
            webTestClient.post().uri("/telegram-bot/12345")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isUnauthorized()
        }
    }

})