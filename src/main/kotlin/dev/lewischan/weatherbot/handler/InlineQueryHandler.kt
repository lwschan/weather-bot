package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.InlineQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class InlineQueryHandler {

    protected abstract fun handleInlineQuery(bot: Bot, inlineQuery: InlineQuery)

    protected val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun execute(bot: Bot, inlineQuery: InlineQuery) {
        logger.info("Handling Telegram bot inline query")
        handleInlineQuery(bot, inlineQuery)
    }
}