package dev.lewischan.weatherbot.platforms.telegram.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.core.location.Location
import dev.lewischan.weatherbot.core.weather.WeatherService
import dev.lewischan.weatherbot.core.weather.model.DailyWeather
import dev.lewischan.weatherbot.core.weather.model.Temperature
import dev.lewischan.weatherbot.platforms.telegram.extension.replyMessage
import java.time.format.DateTimeFormatter

abstract class BaseForecastWeatherCommandHandler(
    private val locationResolver: WeatherCommandLocationResolver,
    private val weatherService: WeatherService,
    private val daysAhead: Int
) : CommandHandler() {

    override fun handleCommand(message: Message) {
        val address = getCommandQuery(message)
        logger.info("Handling forecast weather command${address?.let { " for address $it" } ?: " for default location"}")
        val bot = getBot()
        val location = locationResolver.resolve(bot, message, address) ?: return
        val forecast = weatherService.getDailyForecast(location, daysAhead)
        if (forecast == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = if (address.isNullOrEmpty()) {
                    "Encountered an error fetching the weather forecast for ${location.address}."
                } else {
                    "Could not find the weather forecast for $address."
                },
                replyToMessageId = message.messageId
            )
            return
        }

        sendForecastMessage(bot, message, location, forecast)
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
