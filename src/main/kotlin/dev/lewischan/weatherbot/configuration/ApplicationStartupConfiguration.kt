package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.service.TelegramBotService
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class ApplicationStartupConfiguration(
    val telegramBotService: TelegramBotService
) : ApplicationListener<ApplicationReadyEvent> {

    private val logger = LoggerFactory.getLogger(ApplicationStartupConfiguration::class.java)

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info("Starting Telegram Weather Bot")
        telegramBotService.start()
    }
}