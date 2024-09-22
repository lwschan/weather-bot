package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import org.springframework.stereotype.Component

@Component
class WeatherCommandHandler : CommandHandler() {
    override val command = "w"

    override fun handleCommand(
        bot: Bot,
        message: Message
    ) {
        val chatId = message.chat.id
        val address = message.text!!.replace("/$command",  "").trim()

        if (address.isEmpty()) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                replyToMessageId = message.messageId,
                text = "Please provide an address",
            )
            return
        }

        val text = "Weather command $address"
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = text,
        )
    }
}