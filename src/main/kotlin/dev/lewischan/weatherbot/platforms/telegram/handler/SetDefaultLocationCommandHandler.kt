package dev.lewischan.weatherbot.platforms.telegram.handler

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import dev.lewischan.weatherbot.core.location.LocationService
import dev.lewischan.weatherbot.core.service.UserDefaultLocationService
import dev.lewischan.weatherbot.platforms.telegram.extension.replyMessage
import dev.lewischan.weatherbot.platforms.telegram.service.TelegramUserService
import org.springframework.stereotype.Component

@Component
class SetDefaultLocationCommandHandler(
    val userDefaultLocationService: UserDefaultLocationService,
    val locationService: LocationService,
    val telegramUserService: TelegramUserService
) : CommandHandler() {
    override val command: String = "s"
    override val description: String = "Set a default location for weather requests"

    override fun handleCommand(message: Message) {
        val chatId = ChatId.fromId(message.chat.id)
        val addressQuery = getCommandQuery(message)

        if (message.text == null || addressQuery.isNullOrBlank()) {
            getBot().replyMessage(
                chatId = chatId,
                replyToMessageId = message.messageId,
                text = "Include an address along with this command to set it as your default location for weather requests."
            )
            return
        }

        val geocodeLocation = locationService.geocode(addressQuery)
        if (geocodeLocation == null) {
            getBot().replyMessage(
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
        getBot().replyMessage(
            chatId = chatId,
            replyToMessageId = message.messageId,
            text = "Saved ${geocodeLocation.address} as your default location."
        )
    }
}
