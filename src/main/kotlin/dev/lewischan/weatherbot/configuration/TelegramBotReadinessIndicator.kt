package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.bot.TelegramBot
import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator
import org.springframework.boot.availability.ApplicationAvailability
import org.springframework.boot.availability.AvailabilityState
import org.springframework.boot.availability.ReadinessState
import org.springframework.stereotype.Component

@Component
class TelegramBotReadinessIndicator(
    availability: ApplicationAvailability,
    private val telegramBot: TelegramBot
) : ReadinessStateHealthIndicator(availability) {

    override fun getState(applicationAvailability: ApplicationAvailability?): AvailabilityState? {
        return when(telegramBot.status) {
            TelegramBot.Status.READY -> ReadinessState.ACCEPTING_TRAFFIC
            else -> ReadinessState.REFUSING_TRAFFIC
        }
    }

}