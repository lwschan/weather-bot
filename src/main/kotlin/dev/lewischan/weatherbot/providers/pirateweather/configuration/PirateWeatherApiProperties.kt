package dev.lewischan.weatherbot.providers.pirateweather.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "pirate-weather-api")
data class PirateWeatherApiProperties @ConstructorBinding constructor(
    val baseUrl: String,
    val apiKey: String
)
