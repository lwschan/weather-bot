package dev.lewischan.weatherbot.platforms.telegram.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.types.TelegramBotResult
import dev.lewischan.weatherbot.core.test.BaseIntTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
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

    beforeEach {
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
        every { message.text } returns "/help@test_bot"

        test("Should return the correct response") {
            helpCommandHandler.execute(message)

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

    context("when help command is sent without bot username") {
        val chatId = random.nextLong(1, Long.MAX_VALUE)

        val message = mockk<Message>()
        every { message.chat.id } returns chatId
        every { message.text } returns "/help"

        test("Should not send any response") {
            helpCommandHandler.execute(message)

            message.text shouldBe "/help"
            verify(exactly = 0) {
                bot.sendMessage(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
            }
        }
    }
})
