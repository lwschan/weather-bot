package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import org.springframework.stereotype.Component

@Component
class StartCommandHandler : CommandHandler {
    override val command = "start"

    override fun handleCommand(bot: Bot, message: Message) {
        val chatId = message.chat.id
        val text = "Welcome to the Weather Bot! Please enter a location to get the current weather."
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = text,
        )
    }
}