package dev.lewischan.weatherbot.service

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult
import com.google.maps.places.v1.PlacesClient
import com.google.maps.places.v1.SearchTextRequest
import com.google.maps.places.v1.SearchTextResponse
import dev.lewischan.weatherbot.model.Location
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class GoogleMapsLocationService(
    private val geoApiContext: GeoApiContext,
    @Qualifier("searchPlacesClient") private val searchPlacesClient: PlacesClient
) : LocationService {

    override fun geocode(addressQuery: String): Location? {
        val geoCodeResponse: Array<GeocodingResult> = GeocodingApi.geocode(geoApiContext, addressQuery).await()
        if (geoCodeResponse.isEmpty()) {
            return null
        }

        return Location(
            geoCodeResponse[0].formattedAddress,
            geoCodeResponse[0].geometry.location.lat,
            geoCodeResponse[0].geometry.location.lng
        )
    }

    override fun search(addressQuery: String): List<Location> {
        val request: SearchTextRequest = SearchTextRequest.newBuilder()
            .setTextQuery(addressQuery)
            .build()

        val response: SearchTextResponse = searchPlacesClient.searchText(request)

        return response.placesList.map {
            Location(
                it.formattedAddress,
                it.location.latitude,
                it.location.longitude
            )
        }
    }
}