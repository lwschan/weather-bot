package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.extension.replyMessage
import org.springframework.stereotype.Component

@Component
class StartCommandHandler : CommandHandler() {
    override val command = "start"
    override val description = "Hello world!"

    override fun handleCommand(message: Message) {
        val text = """
            Hello! I am a weather bot!
            
            To find out what I can do, tap on /help@${getBot().getMe().get().username!!}.
        """.trimIndent()

        getBot().replyMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = text,
            parseMode = ParseMode.HTML
        )
    }
}
