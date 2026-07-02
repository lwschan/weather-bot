package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import dev.lewischan.weatherbot.extension.replyMessage
import dev.lewischan.weatherbot.model.CurrentAirQuality
import dev.lewischan.weatherbot.model.CurrentWeather
import dev.lewischan.weatherbot.model.Location
import dev.lewischan.weatherbot.model.Temperature
import dev.lewischan.weatherbot.service.LocationService
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import dev.lewischan.weatherbot.service.WeatherService
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

abstract class BaseWeatherCommandHandler(
    private val userDefaultLocationService: UserDefaultLocationService,
    private val telegramUserService: TelegramUserService,
    private val weatherService: WeatherService,
    private val locationService: LocationService,
    private val includeAirQuality: Boolean
) : CommandHandler() {

    override fun handleCommand(message: Message) {
        val address = getCommandQuery(message)

        if (address.isNullOrEmpty()) handleWithDefaultLocation(getBot(), message)
        else handleWithAddressSearch(getBot(), message, address)
    }

    private fun handleWithDefaultLocation(bot: Bot, message: Message) {
        logger.info("Handling weather command for default location")

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
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "You do not have a default location, either use the command with an address query or set a default location.",
                replyToMessageId = message.messageId
            )
            return
        }

        val userDefaultLocation = userDefaultLocationService.findByUserId(user.id)
        if (userDefaultLocation == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "You do not have a default location, either use the command with an address query or set a default location.",
                replyToMessageId = message.messageId
            )
            return
        }

        val weather = weatherService.getCurrentWeather(userDefaultLocation.location)
        if (weather == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Encountered an error fetching the current weather for ${userDefaultLocation.location.address}.",
                replyToMessageId = message.messageId
            )
            return
        }

        val airQuality = getAirQuality(userDefaultLocation.location)

        sendCurrentWeatherMessage(bot, message, userDefaultLocation.location, weather, airQuality)
    }

    private fun handleWithAddressSearch(bot: Bot, message: Message, address: String) {
        logger.info("Handling weather command for address $address")

        val location = locationService.geocode(address)
        if (location == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Could not find a valid address for $address.",
                replyToMessageId = message.messageId
            )
            return
        }

        val weather = weatherService.getCurrentWeather(location)
        if (weather == null) {
            bot.replyMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = "Could not find the current weather for $address.",
                replyToMessageId = message.messageId
            )
            return
        }

        val airQuality = getAirQuality(location)

        sendCurrentWeatherMessage(bot, message, location, weather, airQuality)
    }

    private fun sendCurrentWeatherMessage(
        bot: Bot,
        message: Message,
        location: Location,
        weather: CurrentWeather,
        airQuality: CurrentAirQuality?
    ) {
        val dailyWeather = weather.dailyWeather
        val airQualitySection = formatAirQualitySection(airQuality)

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

        val airQualityText = airQuality?.let {
            """
                
                <b>AQI (US / EU):</b> ${it.usAqi} / ${it.europeanAqi}
                <b>PM 2.5:</b> ${it.pmTwoPointFive} μg/m³
                <b>PM 10:</b> ${it.pmTen} μg/m³
                <b>UV Index:</b> ${it.uvIndex}
                <b>UV Index Clear Sky:</b> ${it.uvIndexClearSky}
                
                
            """.trimIndent().dropLast(1)
        } ?: "<i>No air quality data available</i>"

        return airQualityText
    }

    private fun getTemperatureEmoji(temperature: Temperature): String {
        return when (temperature.celsius) {
            in Double.NEGATIVE_INFINITY..-10.0 -> "🥶"
            in -10.0..0.0 -> "❄️"
            in 0.1..15.0 -> "🌬️"
            in 15.1..25.0 -> "🙂"
            in 25.1..35.0 -> "☀️"
            in 35.1..40.0 -> "🥵"
            else -> "🔥"
        }
    }

    private fun getAirQuality(location: Location): CurrentAirQuality? {
        if (!includeAirQuality) return null

        try {
            return weatherService.getCurrentAirQuality(location)
        } catch (exception: Exception) {
            logger.error("Error fetching air quality for location $location", exception)
            return null
        }
    }

    companion object {
        const val WEATHER_TEXT_INDENT = "            "
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
        val datetimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM, h:mm a")
    }
}
