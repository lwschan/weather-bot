package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.BaseIntTest
import io.kotest.core.test.AssertionMode
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.security.SecureRandom

class HelpCommandHandlerIntTest(
    private val helpCommandHandler: CommandHandler,
    private val bot: Bot,
    private val commandHandlers: List<CommandHandler>
) : BaseIntTest({

    val random = SecureRandom()

    beforeSpec {
        every { bot.getMe().get().username } returns "test_bot"
        every { bot.getMyCommands().get() } returns commandHandlers.map {
            BotCommand(it.command, it.description)
        }
    }

    afterSpec {
        clearMocks(bot)
    }

    context("when handle command is called with a valid message") {
        val chatId = random.nextLong()

        val message = mockk<Message>()
        every { message.chat.id } returns chatId
        every { message.text } returns ""

        helpCommandHandler.execute(bot, message)

        test("Should return the correct response") {
            verify(exactly = 1) { bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = any(),
                parseMode = ParseMode.HTML
            ) }
        }
    }
}) {
    override fun assertionMode(): AssertionMode? {
        return AssertionMode.None
    }
}
