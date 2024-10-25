package dev.lewischan.weatherbot.controller

import com.github.kotlintelegrambot.entities.Update
import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import io.kotest.core.test.AssertionMode
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.security.SecureRandom

class TelegramBotControllerIntTest(
    private val webTestClient: WebTestClient,
    private val telegramBotProperties: TelegramBotProperties,
) : BaseIntTest({

    val random = SecureRandom()

    test("when token is valid, telegram-bot endpoint should accept the request") {
        webTestClient.post()
            .uri("/telegram-bot/${telegramBotProperties.apiToken}")
            .body(BodyInserters.fromValue(Update(random.nextLong())))
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isOk()
    }

    test("when token is invalid, telegram-bot endpoint should reject the request with unauthorized") {
        webTestClient.post()
            .uri("/telegram-bot/12345")
            .body(BodyInserters.fromValue(Update(random.nextLong())))
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isUnauthorized()
    }

}) {
    override fun assertionMode(): AssertionMode? {
        return AssertionMode.None
    }
}