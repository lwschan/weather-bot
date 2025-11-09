package dev.lewischan.weatherbot.telegram

import com.github.kotlintelegrambot.Bot
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.AssertionMode
import io.mockk.mockk
import io.mockk.verify

class TelegramBotTest : FunSpec({

    test("when webhook is enabled, it should start with webhook") {
        val webhookProperties = TelegramBotProperties(
            apiToken = "12345",
            useWebhook = true,
            serverHostname = "localhost",
            webhookSecretToken = "123456"
        )
        val bot = mockk<Bot>(relaxed = true)
        val telegramBot = TelegramBot(webhookProperties, bot, emptyList())

        telegramBot.start()

        verify(exactly = 1) {
            bot.setMyCommands(any())
            bot.startWebhook()
        }

        telegramBot.stop()
        verify(exactly = 0) {
            bot.stopWebhook()
        }
    }

    test("when polling is enabled, it should start with polling") {
        val webhookProperties = TelegramBotProperties(
            apiToken = "12345",
            useWebhook = false,
            serverHostname = null,
            webhookSecretToken = null
        )
        val bot = mockk<Bot>(relaxed = true)
        val telegramBot = TelegramBot(webhookProperties, bot, emptyList())

        telegramBot.start()

        verify(exactly = 1) {
            bot.setMyCommands(any())
            bot.startPolling()
        }

        telegramBot.stop()
        verify(exactly = 1) {
            bot.stopPolling()
        }
    }
}) {
    override fun assertionMode(): AssertionMode {
        return AssertionMode.None
    }
}