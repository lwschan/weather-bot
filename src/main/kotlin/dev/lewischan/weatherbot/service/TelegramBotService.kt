package dev.lewischan.weatherbot.service

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import dev.lewischan.weatherbot.configuration.ApplicationStartupConfiguration
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import dev.lewischan.weatherbot.handler.CommandHandler
import org.slf4j.LoggerFactory

class TelegramBotService {

    private val logger = LoggerFactory.getLogger(ApplicationStartupConfiguration::class.java)

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
            logger.info("Starting bot in webhook mode")
            telegramBot.startWebhook()
        } else {
            logger.info("Starting bot in polling mode")
            telegramBot.startPolling()
        }
    }

    private fun createTelegramBot(commandHandlers: List<CommandHandler>): Bot {
        return bot {
            token = telegramBotProperties.apiToken
            dispatch {
                commandHandlers.forEach {
                    command(it.command) {
                        it.handleCommand(bot, message)
                    }
                }
            }
        }
    }

}