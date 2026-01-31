package dev.lewischan.weatherbot.google.maps.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "google-maps")
data class GoogleMapsServicesProperties @ConstructorBinding constructor(
    val apiKey: String,
    val apiBaseUrl: String
)

