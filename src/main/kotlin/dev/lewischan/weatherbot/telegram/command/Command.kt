package dev.lewischan.weatherbot.telegram.command

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import dev.lewischan.weatherbot.telegram.TelegramBotProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

abstract class Command {
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
        val messageId = UUID.randomUUID()
        logger.info("[$messageId] Handling Telegram bot command: $command for message: ${message.text}")
        try {
            handleCommand(message)
        } catch (exception: Exception) {
            logger.error(exception.message, exception)
        }
        logger.info("[$messageId] Finished handling Telegram bot command: $command for message: ${message.text}")
    }
}