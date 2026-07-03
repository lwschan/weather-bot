package dev.lewischan.weatherbot.handler

import dev.lewischan.weatherbot.service.LocationService
import dev.lewischan.weatherbot.service.OpenMeteoWeatherService
import dev.lewischan.weatherbot.service.TelegramUserService
import dev.lewischan.weatherbot.service.UserDefaultLocationService
import org.springframework.stereotype.Component

@Component
class TomorrowWeatherCommandHandler(
    userDefaultLocationService: UserDefaultLocationService,
    telegramUserService: TelegramUserService,
    openMeteoWeatherService: OpenMeteoWeatherService,
    locationService: LocationService
) : BaseForecastWeatherCommandHandler(
    userDefaultLocationService,
    telegramUserService,
    openMeteoWeatherService,
    locationService,
    daysAhead = 1
) {
    override val command = "wt"
    override val description = "Get tomorrow's weather for your default location or include an address."
}
