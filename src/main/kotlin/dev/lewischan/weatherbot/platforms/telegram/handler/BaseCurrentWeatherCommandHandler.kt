package dev.lewischan.weatherbot.platforms.telegram.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyParameters
import dev.lewischan.weatherbot.core.location.Location
import dev.lewischan.weatherbot.core.weather.WeatherService
import dev.lewischan.weatherbot.core.weather.model.CurrentAirQuality
import dev.lewischan.weatherbot.core.weather.model.CurrentWeather
import dev.lewischan.weatherbot.core.weather.model.Temperature
import dev.lewischan.weatherbot.platforms.telegram.extension.replyMessage
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

abstract class BaseCurrentWeatherCommandHandler(
    private val locationResolver: WeatherCommandLocationResolver,
    private val weatherService: WeatherService,
    private val includeAirQuality: Boolean
) : CommandHandler() {

    override fun handleCommand(message: Message) {
        val address = getCommandQuery(message)
        logger.info("Handling current weather command${address?.let { " for address $it" } ?: " for default location"}")
        val bot = getBot()
        val location = locationResolver.resolve(bot, message, address) ?: return

        val weather = weatherService.getCurrentWeather(location)
        if (weather == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = if (address.isNullOrEmpty()) {
                    "Encountered an error fetching the current weather for ${location.address}."
                } else {
                    "Could not find the current weather for $address."
                },
                replyParameters = ReplyParameters(messageId = message.messageId)
            )
            return
        }

        sendCurrentWeatherMessage(bot, message, location, weather, getAirQuality(location))
    }

    private fun sendCurrentWeatherMessage(
        bot: Bot,
        message: Message,
        location: Location,
        weather: CurrentWeather,
        airQuality: CurrentAirQuality?
    ) {
        val dailyWeather = weather.dailyWeather
        val weatherText = """
            ${location.address}
            
            <code>${weather.condition.emoji} ${weather.condition.value}</code>
            <code>${weather.temperature.celsius}°C / ${weather.temperature.fahrenheit}°F</code>

            <b>Feels Like:</b> ${weather.feelsLikeTemperature.celsius}°C / ${weather.feelsLikeTemperature.fahrenheit}°F ${getTemperatureEmoji(weather.feelsLikeTemperature)}
            <b>Humidity:</b> ${weather.humidity} 💧
            <blockquote expandable>
            <b>H:</b> ${dailyWeather.dailyTemperature.high.celsius}°C / ${dailyWeather.dailyTemperature.high.fahrenheit}°F
            <b>L:</b> ${dailyWeather.dailyTemperature.low.celsius}°C / ${dailyWeather.dailyTemperature.low.fahrenheit}°F
            
            <b>Feels Like H:</b> ${dailyWeather.dailyFeelsLikeTemperature.high.celsius}°C / ${dailyWeather.dailyFeelsLikeTemperature.high.fahrenheit}°F ${getTemperatureEmoji(dailyWeather.dailyFeelsLikeTemperature.high)}
            <b>Feels Like L:</b> ${dailyWeather.dailyFeelsLikeTemperature.low.celsius}°C / ${dailyWeather.dailyFeelsLikeTemperature.low.fahrenheit}°F ${getTemperatureEmoji(dailyWeather.dailyFeelsLikeTemperature.low)}
            
            <b>Sunrise:</b> ${dailyWeather.sunrise.format(timeFormatter)}
            <b>Sunset:</b> ${dailyWeather.sunset.format(timeFormatter)}
            ${formatAirQualitySection(airQuality).prependIndent(WEATHER_TEXT_INDENT).dropLast(1)}
            <i>${ZonedDateTime.ofInstant(Instant.now(), weather.time.zone).format(datetimeFormatter)}</i>
            </blockquote>
        """.trimIndent()

        bot.replyMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = weatherText,
            parseMode = ParseMode.HTML
        )
    }

    private fun formatAirQualitySection(airQuality: CurrentAirQuality?): String {
        if (!includeAirQuality) return ""

        return airQuality?.let {
            """
                
                <b>AQI (US / EU):</b> ${it.usAqi} / ${it.europeanAqi}
                <b>PM 2.5:</b> ${it.pmTwoPointFive} μg/m³
                <b>PM 10:</b> ${it.pmTen} μg/m³
                <b>UV Index:</b> ${it.uvIndex}
                <b>UV Index Clear Sky:</b> ${it.uvIndexClearSky}
                
                
            """.trimIndent().dropLast(1)
        } ?: "<i>No air quality data available</i>"
    }

    private fun getAirQuality(location: Location): CurrentAirQuality? {
        if (!includeAirQuality) return null

        return try {
            weatherService.getCurrentAirQuality(location)
        } catch (exception: Exception) {
            logger.error("Error fetching air quality for location $location", exception)
            null
        }
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
        private const val WEATHER_TEXT_INDENT = "            "
        private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
        private val datetimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM, h:mm a")
    }
}
