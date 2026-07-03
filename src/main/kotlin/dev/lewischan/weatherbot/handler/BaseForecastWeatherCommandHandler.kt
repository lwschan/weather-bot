package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.extension.replyMessage
import dev.lewischan.weatherbot.model.DailyWeather
import dev.lewischan.weatherbot.model.Location
import dev.lewischan.weatherbot.model.Temperature
import dev.lewischan.weatherbot.service.LocationService
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import dev.lewischan.weatherbot.service.WeatherService
import java.time.format.DateTimeFormatter

abstract class BaseForecastWeatherCommandHandler(
    private val userDefaultLocationService: UserDefaultLocationService,
    private val telegramUserService: TelegramUserService,
    private val weatherService: WeatherService,
    private val locationService: LocationService,
    private val daysAhead: Int
) : CommandHandler() {

    override fun handleCommand(message: Message) {
        val address = getCommandQuery(message)

        if (address.isNullOrEmpty()) handleWithDefaultLocation(getBot(), message)
        else handleWithAddressSearch(getBot(), message, address)
    }

    private fun handleWithDefaultLocation(bot: Bot, message: Message) {
        logger.info("Handling forecast weather command for default location")

        if (message.from == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Encountered an unexpected error.",
                replyToMessageId = message.messageId
            )
            return
        }

        val user = telegramUserService.findByExternalUserId(message.from!!.id)
        if (user == null) {
            sendMissingDefaultLocationMessage(bot, message)
            return
        }

        val userDefaultLocation = userDefaultLocationService.findByUserId(user.id)
        if (userDefaultLocation == null) {
            sendMissingDefaultLocationMessage(bot, message)
            return
        }

        val location = userDefaultLocation.location
        handleForecast(
            bot,
            message,
            location,
            "Encountered an error fetching the weather forecast for ${location.address}."
        )
    }

    private fun handleWithAddressSearch(bot: Bot, message: Message, address: String) {
        logger.info("Handling forecast weather command for address $address")

        val location = locationService.geocode(address)
        if (location == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Could not find a valid address for $address.",
                replyToMessageId = message.messageId
            )
            return
        }

        handleForecast(bot, message, location, "Could not find the weather forecast for $address.")
    }

    private fun handleForecast(bot: Bot, message: Message, location: Location, errorMessage: String) {
        val forecast = weatherService.getDailyForecast(location, daysAhead)
        if (forecast == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = errorMessage,
                replyToMessageId = message.messageId
            )
            return
        }

        sendForecastMessage(bot, message, location, forecast)
    }

    private fun sendMissingDefaultLocationMessage(bot: Bot, message: Message) {
        bot.replyMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = "You do not have a default location, either use the command with an address query or set a default location.",
            replyToMessageId = message.messageId
        )
    }

    private fun sendForecastMessage(bot: Bot, message: Message, location: Location, forecast: DailyWeather) {
        val dayLabel = when (daysAhead) {
            0 -> "Today"
            1 -> "Tomorrow"
            else -> "In $daysAhead days"
        }
        val weatherText = """
            ${location.address}

            <b>$dayLabel · ${forecast.date.format(dateFormatter)}</b>
            
            <code>${forecast.condition.emoji} ${forecast.condition.value}</code>
            <b>Rain:</b> ${forecast.precipitationProbability}% 🌧️
            <blockquote expandable>
            <b>H:</b> ${forecast.dailyTemperature.high.celsius}°C / ${forecast.dailyTemperature.high.fahrenheit}°F
            <b>L:</b> ${forecast.dailyTemperature.low.celsius}°C / ${forecast.dailyTemperature.low.fahrenheit}°F

            <b>Feels Like H:</b> ${forecast.dailyFeelsLikeTemperature.high.celsius}°C / ${forecast.dailyFeelsLikeTemperature.high.fahrenheit}°F ${getTemperatureEmoji(forecast.dailyFeelsLikeTemperature.high)}
            <b>Feels Like L:</b> ${forecast.dailyFeelsLikeTemperature.low.celsius}°C / ${forecast.dailyFeelsLikeTemperature.low.fahrenheit}°F ${getTemperatureEmoji(forecast.dailyFeelsLikeTemperature.low)}

            <b>Sunrise:</b> ${forecast.sunrise.format(timeFormatter)}
            <b>Sunset:</b> ${forecast.sunset.format(timeFormatter)}
            </blockquote>
        """.trimIndent()

        bot.replyMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = weatherText,
            parseMode = ParseMode.HTML
        )
    }

    private fun getTemperatureEmoji(temperature: Temperature): String = when (temperature.celsius) {
        in Double.NEGATIVE_INFINITY..-10.0 -> "🥶"
        in -10.0..0.0 -> "❄️"
        in 0.1..15.0 -> "🌬️"
        in 15.1..25.0 -> "🙂"
        in 25.1..35.0 -> "☀️"
        in 35.1..40.0 -> "🥵"
        else -> "🔥"
    }

    companion object {
        private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM")
    }
}
