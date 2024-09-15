package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.service.TelegramBotService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class ApplicationStartupConfiguration(
    val telegramBotService: TelegramBotService
) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        telegramBotService.start()
    }
}