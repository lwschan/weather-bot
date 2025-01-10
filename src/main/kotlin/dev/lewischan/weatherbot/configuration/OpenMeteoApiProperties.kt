package dev.lewischan.weatherbot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "open-meteo-api")
data class OpenMeteoApiProperties @ConstructorBinding constructor(
    val weatherApiBaseUrl: String,
    val airQualityApiBaseUrl: String
)
