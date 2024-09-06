package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.BaseIntTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class GoogleMapsGeocodingServiceIntTest @Autowired constructor(
    private val googleMapsGeocodingService: GoogleMapsGeocodingService
) : BaseIntTest() {

    @Test
    fun getLocationShouldGeoLocateAddressToLocation() {
        googleMapsGeocodingService.getLocation()
    }
}