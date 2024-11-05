package dev.lewischan.weatherbot.controller

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import io.kotest.core.test.AssertionMode
import io.mockk.clearMocks
import io.mockk.coVerify
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.security.SecureRandom

class TelegramBotControllerIntTest(
    private val webTestClient: WebTestClient,
    private val telegramBotProperties: TelegramBotProperties,
    private val bot: Bot
) : BaseIntTest({

    afterEach {
        clearMocks(bot)
    }

    val random = SecureRandom()

    test("when token is valid, telegram-bot endpoint should accept the request") {
        val updateId = random.nextLong()

        webTestClient.post()
            .uri("/telegram-bot/${telegramBotProperties.apiToken}")
            .body(BodyInserters.fromValue(Update(updateId)))
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isOk()

        coVerify(exactly = 1) { bot.processUpdate(match<Update> {
            it.updateId == updateId
        }) }
    }

    test("when token is invalid, telegram-bot endpoint should reject the request with unauthorized") {
        webTestClient.post()
            .uri("/telegram-bot/12345")
            .body(BodyInserters.fromValue(Update(random.nextLong())))
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isUnauthorized()

        coVerify(exactly = 0) { bot.processUpdate(any(Update::class)) }
    }

}) {
    override fun assertionMode(): AssertionMode? {
        return AssertionMode.None
    }
}