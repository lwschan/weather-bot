package dev.lewischan.weatherbot.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.dispatcher.inlineQuery
import com.github.kotlintelegrambot.entities.BotCommand
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import dev.lewischan.weatherbot.handler.CommandHandler
import dev.lewischan.weatherbot.handler.InlineQueryHandler
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory

class TelegramBot(
    private val telegramBotProperties: TelegramBotProperties,
    commandHandlers: List<CommandHandler>,
    inlineQueryHandler: InlineQueryHandler,
    chosenInlineResulHandler: Handler
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val telegramBot: Bot

    init {
        this.telegramBot = createTelegramBot(commandHandlers, inlineQueryHandler, chosenInlineResulHandler)
        registerCommands(commandHandlers)
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
        inlineQueryHandler: InlineQueryHandler,
        chosenInlineResulHandler: Handler
    ): Bot {
        return bot {
            token = telegramBotProperties.apiToken
            dispatch {
                addHandler(chosenInlineResulHandler)
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

    private fun registerCommands(commandHandlers: List<CommandHandler>) {
        telegramBot.setMyCommands(
            commandHandlers.stream().map {
                BotCommand(it.command, it.description)
            }.toList()
        )
    }

}