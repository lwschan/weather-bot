package dev.lewischan.weatherbot.telegram.command

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.types.TelegramBotResult
import dev.lewischan.weatherbot.UseBaseIntTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.security.SecureRandom

@UseBaseIntTest
class HelpCommandIntTest(
    private val helpCommand: Command,
    private val bot: Bot,
    private val commandHandlers: List<Command>
) : FunSpec({

    val random = SecureRandom()

    beforeSpec {
        val commands: List<BotCommand> = commandHandlers.map {
            BotCommand(it.command, it.description)
        }.toList()

        every { bot.getMe().get().username } returns "test_bot"
        every { bot.getMyCommands() } returns TelegramBotResult.Success(commands)
    }

    afterEach {
        clearMocks(bot)
    }

    context("when handle command is called with a valid message") {
        val chatId = random.nextLong(1, Long.MAX_VALUE)

        val message = mockk<Message>()
        every { message.chat.id } returns chatId
        every { message.text } returns ""

        helpCommand.execute(message)

        test("Should return the correct response") {
            verify(exactly = 1) { bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = match {
                    it shouldContain "<b>Supported Commands</b>"
                    true
                },
                parseMode = ParseMode.HTML
            ) }
        }
    }
})
