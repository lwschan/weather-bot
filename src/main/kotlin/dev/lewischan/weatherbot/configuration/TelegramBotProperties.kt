package dev.lewischan.weatherbot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "telegram-bot")
data class TelegramBotProperties(
    val apiToken: String,
    val useWebhook: Boolean,
    val serverHostname: String?,
    val webhookSecretToken: String?
)
