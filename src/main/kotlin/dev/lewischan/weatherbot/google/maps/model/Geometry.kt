package dev.lewischan.weatherbot.google.maps.model

data class Geometry(
    val bounds: Bounds?,
    val location: LatLng,
    val locationType: String,
    val viewport: Bounds
)

