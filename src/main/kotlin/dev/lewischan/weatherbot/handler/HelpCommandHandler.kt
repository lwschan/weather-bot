package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import org.springframework.stereotype.Component

@Component
class HelpCommandHandler : CommandHandler {
    override val command = "help"

    override fun handleCommand(
        bot: Bot,
        message: Message
    ) {
        val chatId = message.chat.id
        val text = "Help command"
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = text,
        )
    }
}