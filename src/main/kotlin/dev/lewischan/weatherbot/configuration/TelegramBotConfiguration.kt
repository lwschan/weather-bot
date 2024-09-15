package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.handler.CommandHandler
import dev.lewischan.weatherbot.service.TelegramBotService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TelegramBotProperties::class)
class TelegramBotConfiguration {

    @Bean
    fun telegramBotService(
        telegramBotProperties: TelegramBotProperties,
        commandHandlers: List<CommandHandler>
    ): TelegramBotService {
        return TelegramBotService(telegramBotProperties, commandHandlers)
    }
}