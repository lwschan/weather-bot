package dev.lewischan.weatherbot.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import dev.lewischan.weatherbot.handler.CommandHandler
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.core.PropagationContextElement

class TelegramBot(
    private val telegramBotProperties: TelegramBotProperties,
    private val bot: Bot,
    private val commandHandlers: List<CommandHandler>,
    var status: Status = Status.NOT_READY
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun processUpdate(update: String) {
        withContext(Dispatchers.IO + PropagationContextElement()) {
            bot.processUpdate(update)
        }
    }

    fun setup() {
        logger.info("Starting Telegram Weather Bot")

        registerCommands(commandHandlers)

        if (telegramBotProperties.useWebhook) {
            logger.info("Starting webhook")
            bot.startWebhook()
        } else {
            logger.info("Starting polling")
            bot.startPolling()
        }

        logger.info("Telegram bot started successfully")
        setBotStatus(Status.READY)
    }

    @PreDestroy
    fun stop() {
        logger.info("Stopping bot")

        if (!telegramBotProperties.useWebhook) {
            setBotStatus(Status.NOT_READY)
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