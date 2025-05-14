package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import dev.lewischan.weatherbot.bot.TelegramBotProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class CommandHandler {
    abstract val command: String
    abstract val description: String

    protected abstract fun handleCommand(message: Message)

    protected val logger: Logger = LoggerFactory.getLogger(javaClass)

    protected fun getBot(): Bot = TelegramBotProvider.get()

    protected fun getCommandQuery(message: Message): String? {
        return message.text?.replace("/$command", "")
            ?.replace("@${getBot().getMe().get().username}", "")
            ?.trim()
    }

    fun execute(message: Message) {
        logger.info("Handling Telegram bot command: $command for message: ${message.text}")
        try {
            handleCommand(message)
        } catch (exception: Exception) {
            logger.error(exception.message, exception)
        }
    }
}