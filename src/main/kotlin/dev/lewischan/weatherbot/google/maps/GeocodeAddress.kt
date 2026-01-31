package dev.lewischan.weatherbot.google.maps

import dev.lewischan.weatherbot.google.maps.configuration.GoogleMapsServicesProperties
import dev.lewischan.weatherbot.google.maps.exception.GeocodeException
import dev.lewischan.weatherbot.google.maps.model.GeocodeResponse
import dev.lewischan.weatherbot.google.maps.model.GeocodeStatus
import dev.lewischan.weatherbot.model.Location
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class GeocodeAddress(
    private val googleMapsRestClient: RestClient,
    private val googleMapsServicesProperties: GoogleMapsServicesProperties
) {

    fun geocode(address: String): Location? {
        val response = googleMapsRestClient.get()
            .uri("/maps/api/geocode/json?address={address}&key={key}", address, googleMapsServicesProperties.apiKey)
            .retrieve()
            .body<GeocodeResponse>()
            ?: return null

        return when (response.status) {
            GeocodeStatus.OK -> {
                val result = response.results.firstOrNull() ?: return null
                Location(
                    address = result.formattedAddress,
                    latitude = result.geometry.location.lat,
                    longitude = result.geometry.location.lng
                )
            }
            GeocodeStatus.ZERO_RESULTS -> null
            else -> throw GeocodeException("Geocoding failed with status: ${response.status}")
        }
    }
}
