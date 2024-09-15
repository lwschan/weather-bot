package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message

interface CommandHandler {
    val command: String

    fun handleCommand(bot: Bot, message: Message)
}