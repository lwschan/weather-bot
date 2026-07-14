package dev.lewischan.weatherbot.platforms.telegram.handler

import dev.lewischan.weatherbot.providers.weather.openmeteo.service.OpenMeteoWeatherService
import org.springframework.stereotype.Component

@Component
class DefaultWeatherCommandHandler(
    locationResolver: WeatherCommandLocationResolver,
    openMeteoWeatherService: OpenMeteoWeatherService,
) : BaseCurrentWeatherCommandHandler(
    locationResolver,
    openMeteoWeatherService,
    includeAirQuality = true
) {
    override val command = "w"
    override val description = "Get the current weather for your default location or include an address."
}
