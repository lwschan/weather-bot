package dev.lewischan.weatherbot.service

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import org.springframework.stereotype.Service

@Service
class GoogleMapsGeocodingService(
    private val geoApiContext: GeoApiContext
) : GeocodingService {

    override fun getLocation() {
        val response = GeocodingApi.geocode(geoApiContext, "Singapore").await()
    }
}