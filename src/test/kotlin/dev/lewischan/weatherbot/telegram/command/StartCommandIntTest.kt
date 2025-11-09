package dev.lewischan.weatherbot.telegram.command

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.UseBaseIntTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.security.SecureRandom

@UseBaseIntTest
class StartCommandIntTest(
    private val startCommand: Command,
    private val bot: Bot
) : FunSpec({

    val random = SecureRandom()

    beforeSpec {
        every { bot.getMe().get().username } returns "test_bot"
    }

    afterEach {
        clearMocks(bot)
    }

    test("start command should be handled correctly") {
        val chatId = random.nextLong()

        val message = mockk<Message>()
        every { message.chat.id } returns chatId
        every { message.text } returns ""

        startCommand.execute(message)

        verify(exactly = 1) { bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = match {
                it shouldContain "Hello! I am a weather bot!"
                it shouldContain "Tap on /help@test_bot for more information."
                true
            },
            parseMode = ParseMode.HTML
        ) }
    }
})
