package dev.lewischan.weatherbot.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import dev.lewischan.weatherbot.handler.CommandHandler
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory

class TelegramBot(
    private val telegramBotProperties: TelegramBotProperties,
    private val bot: Bot,
    commandHandlers: List<CommandHandler>,
    var status: Status = Status.NOT_READY
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        registerCommands(commandHandlers)
        setBotStatus(Status.READY)
    }

    suspend fun processUpdate(update: String) {
        bot.processUpdate(update)
    }

    fun start() {
        logger.info("Starting Telegram Weather Bot")

        if (telegramBotProperties.useWebhook) {
            logger.info("Starting webhook")
            bot.startWebhook()
        } else {
            logger.info("Starting polling")
            bot.startPolling()
        }

        logger.info("Telegram bot started successfully")
    }

    @PreDestroy
    fun stop() {
        logger.info("Stopping bot")

        setBotStatus(Status.NOT_READY)

        if (!telegramBotProperties.useWebhook) {
            bot.stopPolling()
        }
    }

    private fun registerCommands(commandHandlers: List<CommandHandler>) {
        bot.setMyCommands(
            commandHandlers.stream().map {
                BotCommand(it.command, it.description)
            }.toList()
        )
    }

    private fun setBotStatus(status: Status) {
        this.status = status
    }

    enum class Status {
        READY,
        NOT_READY,
    }

}