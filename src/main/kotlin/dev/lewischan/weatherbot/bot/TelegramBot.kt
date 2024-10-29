package dev.lewischan.weatherbot.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.Update
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import dev.lewischan.weatherbot.handler.CommandHandler
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory

class TelegramBot(
    private val telegramBotProperties: TelegramBotProperties,
    private val telegramBot: Bot,
    commandHandlers: List<CommandHandler>
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        registerCommands(commandHandlers)
    }

    suspend fun processUpdate(update: Update) {
        telegramBot.processUpdate(update)
    }

    fun start() {
        logger.info("Starting Telegram Weather Bot")

        if (telegramBotProperties.useWebhook) {
            logger.info("Starting webhook")
            telegramBot.startWebhook()
        } else {
            logger.info("Starting polling")
            telegramBot.startPolling()
        }

        logger.info("Telegram bot started successfully")
    }

    @PreDestroy
    fun stop() {
        if (telegramBotProperties.useWebhook) {
            logger.info("Stopping webhook")
            telegramBot.stopWebhook()
        } else {
            logger.info("Stopping polling")
            telegramBot.stopPolling()
        }
    }

    private fun registerCommands(commandHandlers: List<CommandHandler>) {
        telegramBot.setMyCommands(
            commandHandlers.stream().map {
                BotCommand(it.command, it.description)
            }.toList()
        )
    }

}