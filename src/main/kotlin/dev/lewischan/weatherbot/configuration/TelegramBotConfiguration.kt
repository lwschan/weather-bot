package dev.lewischan.weatherbot.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TelegramBotProperties::class)
class TelegramBotConfiguration {
}