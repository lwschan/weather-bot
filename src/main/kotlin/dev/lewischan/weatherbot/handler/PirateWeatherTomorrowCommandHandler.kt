package dev.lewischan.weatherbot.handler

import dev.lewischan.weatherbot.service.PirateWeatherWeatherService
import org.springframework.stereotype.Component

@Component
class PirateWeatherTomorrowCommandHandler(
    locationResolver: WeatherCommandLocationResolver,
    pirateWeatherWeatherService: PirateWeatherWeatherService,
) : BaseForecastWeatherCommandHandler(
    locationResolver,
    pirateWeatherWeatherService,
    daysAhead = 1
) {
    override val command = "wpt"
    override val description = "Get tomorrow's weather using Pirate Weather."
}
