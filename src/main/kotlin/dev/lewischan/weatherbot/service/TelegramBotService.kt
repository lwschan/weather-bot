package dev.lewischan.weatherbot.service

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import dev.lewischan.weatherbot.handler.CommandHandler
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory

class TelegramBotService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val telegramBotProperties: TelegramBotProperties
    private val telegramBot: Bot

    constructor(
        telegramBotProperties: TelegramBotProperties,
        commandHandlers: List<CommandHandler>
    ) {
        this.telegramBotProperties = telegramBotProperties
        this.telegramBot = createTelegramBot(commandHandlers)
    }

    fun start() {
        if (telegramBotProperties.useWebhook) {
            logger.info("Starting webhook")
            telegramBot.startWebhook()
        } else {
            logger.info("Starting polling")
            telegramBot.startPolling()
        }
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

    private fun createTelegramBot(commandHandlers: List<CommandHandler>): Bot {
        return bot {
            token = telegramBotProperties.apiToken
            dispatch {
                commandHandlers.forEach {
                    command(it.command) {
                        it.execute(bot, message)
                    }
                }
            }
        }
    }

}