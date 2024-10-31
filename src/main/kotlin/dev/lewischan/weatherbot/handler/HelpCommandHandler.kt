package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import org.springframework.stereotype.Component

@Component
class HelpCommandHandler : CommandHandler() {
    override val command = "help"
    override val description = "Get help"

    override fun handleCommand(bot: Bot, message: Message) {
        val botCommands = bot.getMyCommands().get()
        val botUsername = bot.getMe().get().username!!

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

        bot.sendMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = text,
            parseMode = ParseMode.HTML
        )
    }
}