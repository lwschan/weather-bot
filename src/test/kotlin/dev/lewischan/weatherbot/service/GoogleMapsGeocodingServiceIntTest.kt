package dev.lewischan.weatherbot.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import dev.lewischan.weatherbot.BaseIntTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import wiremock.org.eclipse.jetty.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GoogleMapsGeocodingServiceIntTest @Autowired constructor(
    private val googleMapsGeocodingService: GoogleMapsGeocodingService,
    private val wireMockServer: WireMockServer
) : BaseIntTest() {

    private var testGeocodingResponse: String = ""

    @BeforeAll
    fun setup() {
        testGeocodingResponse = javaClass.getResourceAsStream("/test-geocode-response.json")
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")!!

        wireMockServer.start()
        wireMockServer.stubFor(
            get(urlEqualTo("/maps/api/geocode/json?key=AIzaTestKey&address=Stamford+Bridge%2C+London"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody(testGeocodingResponse)
                    .withStatus(HttpStatus.OK_200))
        )
        wireMockServer.stubFor(
            get(urlEqualTo("/maps/api/geocode/json?key=AIzaTestKey&address=Random+Address"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody("{\"results\":[],\"status\":\"ZERO_RESULTS\"}")
                    .withStatus(HttpStatus.OK_200))
        )
    }

    @AfterAll
    fun cleanup() {
        wireMockServer.stop()
    }

    @Test
    fun getLocationShouldGeoLocateAddressToLocation() {
        val location = googleMapsGeocodingService.getLocation("Stamford Bridge, London")
        assertNotNull(location)
        assertEquals(53.990129, location.latitude)
        assertEquals(-0.9140249, location.longitude)
    }

    @Test
    fun getLocationShouldReturnNullWhenAddressNotFound() {
        val location = googleMapsGeocodingService.getLocation("Random Address")
        assertNull(location)
    }
}