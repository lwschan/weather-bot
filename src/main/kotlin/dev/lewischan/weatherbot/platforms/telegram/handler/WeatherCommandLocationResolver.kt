package dev.lewischan.weatherbot.platforms.telegram.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ReplyParameters
import dev.lewischan.weatherbot.core.location.Location
import dev.lewischan.weatherbot.core.location.LocationService
import dev.lewischan.weatherbot.core.service.UserDefaultLocationService
import dev.lewischan.weatherbot.platforms.telegram.extension.replyMessage
import dev.lewischan.weatherbot.platforms.telegram.service.TelegramUserService
import org.springframework.stereotype.Component

@Component
class WeatherCommandLocationResolver(
    private val userDefaultLocationService: UserDefaultLocationService,
    private val telegramUserService: TelegramUserService,
    private val locationService: LocationService
) {
    fun resolve(bot: Bot, message: Message, address: String?): Location? {
        return if (address.isNullOrEmpty()) resolveDefaultLocation(bot, message)
        else resolveAddress(bot, message, address)
    }

    private fun resolveDefaultLocation(bot: Bot, message: Message): Location? {
        val externalUserId = message.from?.id
        if (externalUserId == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Encountered an unexpected error.",
                replyParameters = ReplyParameters(messageId = message.messageId)
            )
            return null
        }

        val user = telegramUserService.findByExternalUserId(externalUserId)
        val location = user?.let { userDefaultLocationService.findByUserId(it.id)?.location }
        if (location == null) sendMissingDefaultLocationMessage(bot, message)
        return location
    }

    private fun resolveAddress(bot: Bot, message: Message, address: String): Location? {
        val location = locationService.geocode(address)
        if (location == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Could not find a valid address for $address.",
                replyParameters = ReplyParameters(messageId = message.messageId)
            )
        }
        return location
    }

    private fun sendMissingDefaultLocationMessage(bot: Bot, message: Message) {
        bot.replyMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = "You do not have a default location, either use the command with an address query or set a default location.",
            replyParameters = ReplyParameters(messageId = message.messageId)
        )
    }
}
