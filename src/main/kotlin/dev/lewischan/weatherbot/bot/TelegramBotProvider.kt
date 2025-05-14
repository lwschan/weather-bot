package dev.lewischan.weatherbot.bot

import com.github.kotlintelegrambot.Bot

class TelegramBotProvider {
    companion object {
        private var bot: Bot? = null

        fun set(bot: Bot) {
            Companion.bot = bot
        }

        fun get(): Bot {
            return bot ?: throw IllegalStateException("Telegram bot is not initialized")
        }
    }
}