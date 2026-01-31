package dev.lewischan.weatherbot.google.maps.model

data class GeocodeResult(
    val addressComponents: List<AddressComponent>,
    val formattedAddress: String,
    val geometry: Geometry,
    val placeId: String,
    val types: List<String>
)

