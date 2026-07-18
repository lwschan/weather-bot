package dev.lewischan.weatherbot.platforms.telegram.handler

import dev.lewischan.weatherbot.providers.weather.openmeteo.service.OpenMeteoWeatherService
import org.springframework.stereotype.Component

@Component
class TomorrowWeatherCommandHandler(
    locationResolver: WeatherCommandLocationResolver,
    openMeteoWeatherService: OpenMeteoWeatherService,
) : BaseForecastWeatherCommandHandler(
    locationResolver,
    openMeteoWeatherService,
    daysAhead = 1
) {
    override val command = "wt"
    override val description = "Get tomorrow's weather for your default location or include an address."
}
