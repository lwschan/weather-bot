package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.UseBaseIntTest
import dev.lewischan.weatherbot.telegram.configuration.TelegramBotProperties
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@UseBaseIntTest
class TelegramBotPropertiesIntTest(
    private val telegramBotProperties: TelegramBotProperties
) : FunSpec({

    test("telegram bot properties should be set correctly") {
        telegramBotProperties.apiToken shouldBe "123456789:ABCDEFGHIJKLMNOPQRSTUVWXYZ123456"
        telegramBotProperties.useWebhook shouldBe true
        telegramBotProperties.serverHostname shouldBe "localhost"
        telegramBotProperties.webhookSecretToken shouldBe "6879e9ec-063e-4e9f-be47-d1982bf340fa"
    }

})
