package dev.lewischan.weatherbot.configuration

import com.github.kotlintelegrambot.Bot
import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator
import org.springframework.boot.availability.ApplicationAvailability
import org.springframework.boot.availability.AvailabilityState
import org.springframework.boot.availability.ReadinessState
import org.springframework.stereotype.Component

@Component
class BotReadinessIndicator(
    availability: ApplicationAvailability,
    bot: Bot
) : ReadinessStateHealthIndicator(availability) {

    override fun getState(applicationAvailability: ApplicationAvailability?): AvailabilityState? {
        if (bot.)

        return ReadinessState.REFUSING_TRAFFIC
    }

}