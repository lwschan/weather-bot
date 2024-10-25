package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.bot.TelegramBot
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class ApplicationStartupConfiguration(
    val telegramBot: TelegramBot
) : ApplicationListener<ApplicationReadyEvent> {

    private val logger = LoggerFactory.getLogger(ApplicationStartupConfiguration::class.java)

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info("Application ready event fired")
        telegramBot.start()
    }
}