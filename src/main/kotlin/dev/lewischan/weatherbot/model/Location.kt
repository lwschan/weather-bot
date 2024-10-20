package dev.lewischan.weatherbot.model

data class Location(
    val name: String,
    val formattedAddress: String,
    val latitude: Double,
    val longitude: Double
)
