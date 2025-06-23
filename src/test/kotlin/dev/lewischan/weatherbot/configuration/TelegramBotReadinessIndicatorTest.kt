package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.bot.TelegramBot
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.actuate.health.Status
import org.springframework.boot.availability.ApplicationAvailability

class TelegramBotReadinessIndicatorTest : FunSpec({

    context("telegram bot readiness indicator should return the correct state") {
        withData(
            Pair(TelegramBot.Status.NOT_READY, Status.OUT_OF_SERVICE),
            Pair(TelegramBot.Status.READY, Status.UP)
        ) { (botStatus, expectedServiceStatus) ->
            val applicationAvailability = mockk<ApplicationAvailability>()

            val telegramBot = mockk<TelegramBot>()
            every { telegramBot.status } returns botStatus

            val readinessIndicator = TelegramBotReadinessIndicator(applicationAvailability, telegramBot)
            readinessIndicator.health().status.code shouldBe expectedServiceStatus.code
        }
    }
})