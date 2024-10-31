package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class CommandHandler {
    abstract val command: String
    abstract val description: String

    protected abstract fun handleCommand(bot: Bot, message: Message)

    protected val logger: Logger = LoggerFactory.getLogger(javaClass)

    protected fun getCommandQuery(bot: Bot, message: Message): String? {
        return message.text?.replace("/$command", "")
            ?.replace("@${bot.getMe().get().username}", "")
            ?.trim()
    }

    fun execute(bot: Bot, message: Message) {
        logger.info("Handling Telegram bot command: $command for message: ${message.text}")
        try {
            handleCommand(bot, message)
        } catch (exception: RuntimeException) {
            logger.error(exception.message, exception)
        }
    }
}