package dev.lewischan.weatherbot.service

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import com.google.maps.places.v1.PlacesClient
import com.google.maps.places.v1.SearchTextRequest
import com.google.maps.places.v1.SearchTextResponse
import dev.lewischan.weatherbot.model.Location
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GoogleMapsLocationService(
    private val geoApiContext: GeoApiContext,
    private val placesClient: PlacesClient
) : LocationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun geocode(addressQuery: String): Location? {
        val response: Array<GeocodingResult> = GeocodingApi.geocode(geoApiContext, addressQuery).await()
        if (response.isEmpty()) {
            return null
        }

        val latLng: LatLng = response[0].geometry.location
        return Location(
            response[0].formattedAddress,
            response[0].formattedAddress,
            latLng.lat,
            latLng.lng
        )
    }

    override fun search(addressQuery: String): List<Location> {
        val request: SearchTextRequest = SearchTextRequest.newBuilder()
            .setTextQuery(addressQuery)
            .build()

        val response: SearchTextResponse = placesClient.searchText(request)

        return response.placesList.map {
            Location(
                it.displayName.text,
                it.formattedAddress,
                it.location.latitude,
                it.location.longitude
            )
        }
    }
}