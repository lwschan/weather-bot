package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.service.LocationService
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import dev.lewischan.weatherbot.service.WeatherService
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class WeatherCommandHandler(
    private val userDefaultLocationService: UserDefaultLocationService,
    private val telegramUserService: TelegramUserService,
    private val weatherService: WeatherService,
    private val locationService: LocationService
) : CommandHandler() {
    override val command = "w"
    override val description = "Get current weather, provide an address if this is not for your default location."

    override fun handleCommand(bot: Bot, message: Message) {
        val address = getCommandQuery(bot, message)

        if (address.isNullOrEmpty()) handleWithDefaultLocation(bot, message)
        else handleWithAddressSearch(bot, message, address)
    }

    private fun handleWithDefaultLocation(bot: Bot, message: Message) {
        logger.info("Handling weather command for default location")

        if (message.from == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Encountered an unexpected error.",
                replyToMessageId = message.messageId
            )
            return
        }

        val user = telegramUserService.findByExternalUserId(message.from!!.id)
        if (user == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "You do not have a default location, either use the command with an address query or set a default location.",
                replyToMessageId = message.messageId
            )
            return
        }

        val userDefaultLocation = userDefaultLocationService.findByUserId(user.id)
        if (userDefaultLocation == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "You do not have a default location, either use the command with an address query or set a default location.",
                replyToMessageId = message.messageId
            )
            return
        }

        val weather = weatherService.getCurrentWeather(userDefaultLocation.location)
        if (weather == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Encountered an error fetching the current weather for ${userDefaultLocation.location.address}.",
                replyToMessageId = message.messageId
            )
        }

        val localisedTime = weather!!.time.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"))

        bot.sendMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = """                
                ${userDefaultLocation.location.address}
                $localisedTime
                
                🌡️ <b>Temperature:</b> ${weather.temperature.celsius}°C | ${weather.temperature.fahrenheit}°F
                💧 <b>Humidity:</b> ${weather.humidity}
                🥵️ <b>Feels Like:</b> ${weather.feelsLikeTemperature.celsius}°C | ${weather.feelsLikeTemperature.fahrenheit}°F
            """.trimIndent(),
            parseMode = ParseMode.HTML
        ).onError {
            logger.error(it.toString())
        }
    }

    private fun handleWithAddressSearch(bot: Bot, message: Message, address: String) {
        logger.info("Handling weather command for address $address")

        val location = locationService.geocode(address)
        if (location == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Could not find a valid address for $address.",
                replyToMessageId = message.messageId
            )
            return
        }

        val weather = weatherService.getCurrentWeather(location)
        if (weather == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Could not find the current weather for $address.",
                replyToMessageId = message.messageId
            )
            return
        }

        val localisedTime = weather.time.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"))

        bot.sendMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = """
                **Weather Report**           
                
                ${location.address}
                $localisedTime
                
                🌡️ **Temperature:** ${weather.temperature.celsius}°C | ${weather.temperature.fahrenheit}°F
                🌡️ **Feels Like:** ${weather.feelsLikeTemperature.celsius}°C | ${weather.feelsLikeTemperature.fahrenheit}°F
                💧 **Humidity:** ${weather.humidity}
            """.trimIndent(),
            parseMode = ParseMode.MARKDOWN_V2
        )
    }


}