package dev.lewischan.weatherbot.platforms.telegram.handler

import dev.lewischan.weatherbot.providers.weather.pirateweather.service.PirateWeatherWeatherService
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
