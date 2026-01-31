package dev.lewischan.weatherbot.google.maps.model

data class GeocodeResponse(
    val results: List<GeocodeResult>,
    val status: GeocodeStatus
)

