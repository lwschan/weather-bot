package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import io.kotest.core.annotation.DoNotParallelize
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.security.SecureRandom

@DoNotParallelize
class SetDefaultLocationCommandHandlerIntTest(
    private val setDefaultLocationCommandHandler: CommandHandler,
    private val bot: Bot,
    private val telegramUserService: TelegramUserService,
    private val userDefaultLocationService: UserDefaultLocationService
) : BaseIntTest({

    val random = SecureRandom()

    beforeSpec {
        every { bot.getMe().get().username } returns "test_bot"
    }

    afterSpec {
        clearMocks(bot)
    }

    context("when a valid message is received") {
        val chatId = random.nextLong(1, Long.MAX_VALUE)
        val messageId = random.nextLong(1, Long.MAX_VALUE)
        val userId = random.nextLong(1, Long.MAX_VALUE)
        val addressQuery = "Stamford Bridge, London"

        val message = mockk<Message>()
        every { message.chat.id } returns chatId
        every { message.text } returns "/s $addressQuery"
        every { message.messageId } returns messageId
        every { message.from!!.id } returns userId

        test("it should create user and save default location") {
            setDefaultLocationCommandHandler.execute(bot, message)

            val user = telegramUserService.findByExternalUserId(userId)
            user shouldNotBe null

            val defaultLocation = userDefaultLocationService.findByUserId(user!!.id)
            defaultLocation shouldNotBe null
            defaultLocation!!.location.address shouldBe "Stamford Bridge, York YO41, UK"
            defaultLocation.location.latitude shouldBe 53.990129
            defaultLocation.location.longitude shouldBe -0.9140249

            verify(exactly = 1) { bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                replyToMessageId = messageId,
                text = match {
                    it shouldBe "Saved Stamford Bridge, York YO41, UK as your default location."
                    true
                }
            ) }
        }

        test("when user already exists, it should just save default location") {
            val user = telegramUserService.createUser(userId)
            setDefaultLocationCommandHandler.execute(bot, message)

            val defaultLocation = userDefaultLocationService.findByUserId(user.id)
            defaultLocation shouldNotBe null
            defaultLocation!!.location.address shouldBe "Stamford Bridge, York YO41, UK"
            defaultLocation.location.latitude shouldBe 53.990129
            defaultLocation.location.longitude shouldBe -0.9140249

            verify(exactly = 1) { bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                replyToMessageId = messageId,
                text = match {
                    it shouldBe "Saved Stamford Bridge, York YO41, UK as your default location."
                    true
                }
            ) }
        }
    }

    context("when command does not contain an address") {
        val chatId = random.nextLong(1, Long.MAX_VALUE)
        val messageId = random.nextLong(1, Long.MAX_VALUE)
        val userId = random.nextLong(1, Long.MAX_VALUE)

        val message = mockk<Message>()
        every { message.chat.id } returns chatId
        every { message.text } returns "/s"
        every { message.messageId } returns messageId
        every { message.from!!.id } returns userId

        test("it should return an error message") {
            setDefaultLocationCommandHandler.execute(bot, message)

            verify(exactly = 1) { bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                replyToMessageId = messageId,
                text = match {
                    it shouldBe "Include an address along with this command to set it as your default location for weather requests."
                    true
                }
            ) }
        }
    }

    context("when address is invalid") {
        val chatId = random.nextLong(1, Long.MAX_VALUE)
        val messageId = random.nextLong(1, Long.MAX_VALUE)
        val userId = random.nextLong(1, Long.MAX_VALUE)
        val addressQuery = "Random Address"

        val message = mockk<Message>()
        every { message.chat.id } returns chatId
        every { message.text } returns "/s $addressQuery"
        every { message.messageId } returns messageId
        every { message.from!!.id } returns userId

        test("it should return an invalid address error message") {
            setDefaultLocationCommandHandler.execute(bot, message)

            verify(exactly = 1) { bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                replyToMessageId = messageId,
                text = match {
                    it shouldBe "Error: could not find a valid address for $addressQuery."
                    true
                }
            ) }
        }
    }

}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest
}