package dev.lewischan.weatherbot.platforms.telegram.handler

import dev.lewischan.weatherbot.providers.pirateweather.service.PirateWeatherWeatherService
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
