package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.model.CurrentAirQuality
import dev.lewischan.weatherbot.model.CurrentWeather
import dev.lewischan.weatherbot.model.DailyWeather
import dev.lewischan.weatherbot.model.Location

interface WeatherService {
    fun getCurrentWeather(location: Location): CurrentWeather?

    fun getDailyForecast(location: Location, daysAhead: Int): DailyWeather?

    fun getCurrentAirQuality(location: Location): CurrentAirQuality?
}
