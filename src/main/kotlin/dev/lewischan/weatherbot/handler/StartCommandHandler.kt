package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.extension.replyMessage
import org.springframework.stereotype.Component

@Component
class StartCommandHandler : CommandHandler() {
    override val command = "start"
    override val description = "Hello world!"

    override fun handleCommand(bot: Bot, message: Message) {
        val text = """
            Hello! I am a weather bot!
            
            To find out what I can do, tap on /help@${bot.getMe().get().username!!}.
        """.trimIndent()

        bot.replyMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = text,
            parseMode = ParseMode.HTML
        )
    }
}
