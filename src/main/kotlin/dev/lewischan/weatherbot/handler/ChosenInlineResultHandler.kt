package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.entities.Update
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ChosenInlineResultHandler : Handler {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun checkUpdate(update: Update): Boolean {
        return update.chosenInlineResult != null
    }

    override suspend fun handleUpdate(bot: Bot, update: Update) {
        logger.info("${update.chosenInlineResult?.query}")
    }
}