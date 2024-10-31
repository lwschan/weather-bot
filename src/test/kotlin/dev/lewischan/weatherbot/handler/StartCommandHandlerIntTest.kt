package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.BaseIntTest
import io.kotest.matchers.string.match
import io.kotest.matchers.string.shouldContain
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.security.SecureRandom

class StartCommandHandlerIntTest(
    private val startCommandHandler: CommandHandler,
    private val bot: Bot
) : BaseIntTest({

    val random = SecureRandom()

    beforeSpec {
        every { bot.getMe().get().username } returns "test_bot"
    }

    afterSpec {
        clearMocks(bot)
    }

    test("start command should be handled correctly") {
        val chatId = random.nextLong()

        val message = mockk<Message>()
        every { message.chat.id } returns chatId
        every { message.text } returns ""

        startCommandHandler.execute(bot, message)

        verify(exactly = 1) { bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = match {
                it shouldContain "Hello! I am a weather bot!"
                it shouldContain "To find out what I can do, tap on /help@test_bot"
                true
            },
            parseMode = ParseMode.HTML
        ) }
    }
})
