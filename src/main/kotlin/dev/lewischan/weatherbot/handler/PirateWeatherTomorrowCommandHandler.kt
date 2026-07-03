package dev.lewischan.weatherbot.handler

import dev.lewischan.weatherbot.service.LocationService
import dev.lewischan.weatherbot.service.PirateWeatherWeatherService
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import org.springframework.stereotype.Component

@Component
class PirateWeatherTomorrowCommandHandler(
    userDefaultLocationService: UserDefaultLocationService,
    telegramUserService: TelegramUserService,
    pirateWeatherWeatherService: PirateWeatherWeatherService,
    locationService: LocationService
) : BaseForecastWeatherCommandHandler(
    userDefaultLocationService,
    telegramUserService,
    pirateWeatherWeatherService,
    locationService,
    daysAhead = 1
) {
    override val command = "wpt"
    override val description = "Get tomorrow's weather using Pirate Weather."
}
