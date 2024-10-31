package dev.lewischan.weatherbot

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import wiremock.org.eclipse.jetty.http.HttpStatus

class WireMockServerConfiguration(
    private val wireMockServer: WireMockServer
) {
    fun initMock() {
        initMockForGoogleMapsGeocode()
    }

    private fun initMockForGoogleMapsGeocode() {
        val baseUrl = "/maps/api/geocode/json?key=AIzaTestKey&address="

        val testGeocodeResponse = javaClass.getResourceAsStream("/test-geocode-response.json")
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")!!

        wireMockServer.stubFor(
            get(urlEqualTo("${baseUrl}Stamford+Bridge%2C+London"))
                .willReturn(
                    aResponse().withHeader("Content-Type", "application/json")
                        .withBody(testGeocodeResponse)
                        .withStatus(HttpStatus.OK_200))
        )

        val testGeocodeNullResponse = javaClass.getResourceAsStream("/test-geocode-null-response.json")
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")!!

        wireMockServer.stubFor(
            get(urlEqualTo("${baseUrl}Random+Address"))
                .willReturn(
                    aResponse().withHeader("Content-Type", "application/json")
                        .withBody(testGeocodeNullResponse)
                        .withStatus(HttpStatus.OK_200))
        )
    }
}