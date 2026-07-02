package dev.lewischan.weatherbot.handler

import dev.lewischan.weatherbot.service.LocationService
import dev.lewischan.weatherbot.service.OpenMeteoWeatherService
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import org.springframework.stereotype.Component

@Component
class DefaultWeatherCommandHandler(
    userDefaultLocationService: UserDefaultLocationService,
    telegramUserService: TelegramUserService,
    openMeteoWeatherService: OpenMeteoWeatherService,
    locationService: LocationService
) : BaseWeatherCommandHandler(
    userDefaultLocationService,
    telegramUserService,
    openMeteoWeatherService,
    locationService,
    includeAirQuality = true
) {
    override val command = "w"
    override val description = "Get the current weather for your default location or include an address."
}
