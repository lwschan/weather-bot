package dev.lewischan.weatherbot.telegram.command

import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.extension.replyMessage
import org.springframework.stereotype.Component

@Component
class HelpCommand : Command() {
    override val command = "help"
    override val description = "Get help"

    override fun handleCommand(message: Message) {
        val botCommands = getBot().getMyCommands().get()
        val botUsername = getBot().getMe().get().username!!

        val commandDescriptions = botCommands.filter { it.command != command }
            .sortedWith(
                compareBy<BotCommand> { it.command == "w" }
                    .thenBy { it.command == "s" }
                    .thenBy { it.command == "start" }
            )
            .map { "/${it.command}@$botUsername - ${it.description}" }
            .toList()

        val text = """
            <b>Supported Commands</b>
            
            
        """.trimIndent() + commandDescriptions.joinToString("\n")

        getBot().replyMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = text,
            parseMode = ParseMode.HTML
        )
    }
}