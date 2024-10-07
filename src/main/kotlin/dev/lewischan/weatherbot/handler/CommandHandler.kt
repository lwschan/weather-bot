package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import org.slf4j.LoggerFactory

abstract class CommandHandler {
    abstract val command: String

    protected abstract fun handleCommand(bot: Bot, message: Message)

    protected val logger = LoggerFactory.getLogger(javaClass)

    fun execute(bot: Bot, message: Message) {
        logger.info("Handling Telegram bot command: $command for message: ${message.text}")
        handleCommand(bot, message)
    }
}