package dev.lewischan.weatherbot.core.service

import dev.lewischan.weatherbot.core.model.CurrentAirQuality
import dev.lewischan.weatherbot.core.model.CurrentWeather
import dev.lewischan.weatherbot.core.model.DailyWeather
import dev.lewischan.weatherbot.locations.Location

interface WeatherService {
    fun getCurrentWeather(location: Location): CurrentWeather?

    fun getDailyForecast(location: Location, daysAhead: Int): DailyWeather?

    fun getCurrentAirQuality(location: Location): CurrentAirQuality?
}
