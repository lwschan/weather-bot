package dev.lewischan.weatherbot.handler

import dev.lewischan.weatherbot.service.PirateWeatherWeatherService
import org.springframework.stereotype.Component

@Component
class PirateWeatherWeatherCommandHandler(
    locationResolver: WeatherCommandLocationResolver,
    pirateWeatherWeatherService: PirateWeatherWeatherService,
) : BaseCurrentWeatherCommandHandler(
    locationResolver,
    pirateWeatherWeatherService,
    includeAirQuality = false
) {
    override val command = "wp"
    override val description = "Get current weather, but use Pirate Weather as the provider"
}
