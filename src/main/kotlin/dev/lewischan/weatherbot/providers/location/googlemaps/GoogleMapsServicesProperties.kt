package dev.lewischan.weatherbot.providers.location.googlemaps

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "google-maps-services")
data class GoogleMapsServicesProperties @ConstructorBinding constructor(
    val apiKey: String,
    val queryRateLimit: Int
)
