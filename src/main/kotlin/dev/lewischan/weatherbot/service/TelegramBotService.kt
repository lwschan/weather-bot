package dev.lewischan.weatherbot.service

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.inlineQuery
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import dev.lewischan.weatherbot.handler.CommandHandler
import dev.lewischan.weatherbot.handler.InlineQueryHandler
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory

class TelegramBotService(
    private val telegramBotProperties: TelegramBotProperties,
    commandHandlers: List<CommandHandler>,
    inlineQueryHandler: InlineQueryHandler
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val telegramBot: Bot

    init {
        this.telegramBot = createTelegramBot(commandHandlers, inlineQueryHandler)
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

    private fun createTelegramBot(
        commandHandlers: List<CommandHandler>,
        inlineQueryHandler: InlineQueryHandler
    ): Bot {
        return bot {
            token = telegramBotProperties.apiToken
            dispatch {
                commandHandlers.forEach {
                    command(it.command) {
                        it.execute(bot, message)
                    }
                }
                inlineQuery {
                    inlineQueryHandler.execute(bot, inlineQuery)
                }
            }
        }
    }

}