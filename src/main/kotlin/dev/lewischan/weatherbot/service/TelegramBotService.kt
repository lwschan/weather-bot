package dev.lewischan.weatherbot.service

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import dev.lewischan.weatherbot.configuration.TelegramBotProperties


class TelegramBotService(
    private val telegramBotProperties: TelegramBotProperties
) {

    val telegramBot: Bot = bot {
        token = telegramBotProperties.apiToken
        dispatch {
            command("start") {
                message.messageId
                update.message.messageId
            }
        }
    }



}