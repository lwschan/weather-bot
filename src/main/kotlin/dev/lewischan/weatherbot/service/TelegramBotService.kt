package dev.lewischan.weatherbot.service

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.configuration.TelegramBotProperties

class TelegramBotService(
    private val telegramBotProperties: TelegramBotProperties
) {

    val telegramBot: Bot = bot {
        token = telegramBotProperties.apiToken
        dispatch {
            command("start") {
                bot.sendMessage(ChatId.fromId(message.chat.id), "Hello!", ParseMode.MARKDOWN_V2)
            }
        }
    }

    fun start() {
        if (telegramBotProperties.useWebhook) {
            telegramBot.startWebhook()
        } else {
            telegramBot.startPolling()
        }
    }

}