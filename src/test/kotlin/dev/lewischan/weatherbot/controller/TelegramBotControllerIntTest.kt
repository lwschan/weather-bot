package dev.lewischan.weatherbot.controller

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import io.kotest.core.test.AssertionMode
import io.mockk.clearMocks
import io.mockk.coVerify
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
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

    val objectMapper = jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)

    test("when token is valid, telegram endpoint should accept the request") {
        val updateId = random.nextLong()

        webTestClient.post()
            .uri("/telegram/${telegramBotProperties.apiToken}")
            .bodyValue(objectMapper.writeValueAsString(Update(updateId)))
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isOk()

        coVerify(exactly = 1) { bot.processUpdate(match<Update> {
            it.updateId == updateId
        }) }
    }

    test("when token is invalid, telegram endpoint should reject the request with unauthorized") {
        webTestClient.post()
            .uri("/telegram/12345")
            .bodyValue(objectMapper.writeValueAsString(Update(random.nextLong())))
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isUnauthorized()

        coVerify(exactly = 0) { bot.processUpdate(any(Update::class)) }
    }

}) {
    override fun assertionMode(): AssertionMode? {
        return AssertionMode.None
    }
}