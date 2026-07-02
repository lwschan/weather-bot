package dev.lewischan.weatherbot.handler

import dev.lewischan.weatherbot.service.LocationService
import dev.lewischan.weatherbot.service.PirateWeatherWeatherService
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import org.springframework.stereotype.Component

@Component
class PirateWeatherWeatherCommandHandler(
    userDefaultLocationService: UserDefaultLocationService,
    telegramUserService: TelegramUserService,
    pirateWeatherWeatherService: PirateWeatherWeatherService,
    locationService: LocationService
) : BaseWeatherCommandHandler(
    userDefaultLocationService,
    telegramUserService,
    pirateWeatherWeatherService,
    locationService,
    includeAirQuality = false
) {
    override val command = "wp"
    override val description = "Get current weather, but use Pirate Weather as the provider"
}
