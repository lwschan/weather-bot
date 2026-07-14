package dev.lewischan.weatherbot.core.weather

import dev.lewischan.weatherbot.core.location.Location
import dev.lewischan.weatherbot.core.weather.model.CurrentAirQuality
import dev.lewischan.weatherbot.core.weather.model.CurrentWeather
import dev.lewischan.weatherbot.core.weather.model.DailyWeather

interface WeatherService {
    fun getCurrentWeather(location: Location): CurrentWeather?

    fun getDailyForecast(location: Location, daysAhead: Int): DailyWeather?

    fun getCurrentAirQuality(location: Location): CurrentAirQuality?
}
