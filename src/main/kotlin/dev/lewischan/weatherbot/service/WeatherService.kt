package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.model.CurrentWeather
import dev.lewischan.weatherbot.model.Location

interface WeatherService {
    fun getCurrentWeather(location: Location): CurrentWeather?
}