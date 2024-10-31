package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import dev.lewischan.weatherbot.service.LocationService
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import org.springframework.stereotype.Component

@Component
class SetDefaultLocationCommandHandler(
    val userDefaultLocationService: UserDefaultLocationService,
    val locationService: LocationService,
    val telegramUserService: TelegramUserService
) : CommandHandler() {
    override val command: String = "s"
    override val description: String = "Set a default weather location"

    override fun handleCommand(bot: Bot, message: Message) {
        val chatId = ChatId.fromId(message.chat.id)
        if (message.text == null) {
            bot.sendMessage(
                chatId = chatId,
                replyToMessageId = message.messageId,
                text = "Include an address along with this command to set it as your default location for weather requests."
            )
            return
        }

        val addressQuery = getCommandQuery(bot, message)!!

        val geocodeLocation = locationService.geocode(addressQuery)
        if (geocodeLocation == null) {
            bot.sendMessage(
                chatId = chatId,
                replyToMessageId = message.messageId,
                text = "Error: could not find a valid address for $addressQuery."
            )
            return
        }

        val externalUserId = message.from!!.id
        val user = telegramUserService.findByExternalUserId(externalUserId)
            ?: telegramUserService.createUser(externalUserId)

        userDefaultLocationService.save(user.id, geocodeLocation)
        bot.sendMessage(
            chatId = chatId,
            replyToMessageId = message.messageId,
            text = "Saved ${geocodeLocation.address} as your default location."
        )
    }
}