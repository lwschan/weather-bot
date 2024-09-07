package dev.lewischan.weatherbot.service

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import dev.lewischan.weatherbot.model.Location
import org.springframework.stereotype.Service

@Service
class GoogleMapsGeocodingService(
    private val geoApiContext: GeoApiContext
) : GeocodingService {

    override fun getLocation(addressQuery: String): Location? {
        val response: Array<GeocodingResult> = GeocodingApi.geocode(geoApiContext, addressQuery).await()
        if (response.isEmpty()) {
            return null
        }
        val latLng: LatLng = response[0].geometry.location
        return Location(latLng.lat, latLng.lng)
    }
}