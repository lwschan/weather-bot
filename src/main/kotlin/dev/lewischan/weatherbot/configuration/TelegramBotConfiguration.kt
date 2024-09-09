package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.service.TelegramBotService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TelegramBotProperties::class)
class TelegramBotConfiguration {

    @Bean
    fun telegramBotService(telegramBotProperties: TelegramBotProperties): TelegramBotService {
        return TelegramBotService(telegramBotProperties)
    }
}