package dev.lewischan.weatherbot.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.google.api.gax.grpc.testing.MockServiceHelper
import com.google.maps.places.v1.MockPlaces
import com.google.maps.places.v1.SearchTextResponse
import dev.lewischan.weatherbot.BaseIntTestNew
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import wiremock.org.eclipse.jetty.http.HttpStatus


class GoogleMapsLocationServiceIntTest(
    private val googleMapsLocationService: GoogleMapsLocationService,
    private val wireMockServer: WireMockServer,
    private val mockPlacesServiceHelper: MockServiceHelper,
    private val mockPlaces: MockPlaces
) : BaseIntTestNew({

    beforeTest {
        wireMockServer.start()
        mockPlacesServiceHelper.start()

        val testGeocodeResponse = javaClass.getResourceAsStream("/test-geocode-response.json")
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")!!

        wireMockServer.stubFor(
            get(urlEqualTo("/maps/api/geocode/json?key=AIzaTestKey&address=Stamford+Bridge%2C+London"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody(testGeocodeResponse)
                    .withStatus(HttpStatus.OK_200))
        )

        val testGeocodeNullResponse = javaClass.getResourceAsStream("/test-geocode-null-response.json")
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")!!

        wireMockServer.stubFor(
            get(urlEqualTo("/maps/api/geocode/json?key=AIzaTestKey&address=Random+Address"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody(testGeocodeNullResponse)
                    .withStatus(HttpStatus.OK_200))
        )
    }

    afterTest {
        wireMockServer.stop()
        mockPlacesServiceHelper.stop()
    }

    test("geocode should convert the address to a location") {
        val location = googleMapsLocationService.geocode("Stamford Bridge, London")
        location shouldNotBe null
        location?.name shouldBe "Stamford Bridge, York YO41, UK"
        location?.formattedAddress shouldBe "Stamford Bridge, York YO41, UK"
        location?.latitude shouldBe 53.990129
        location?.longitude shouldBe -0.9140249
    }

    test("geocode should return null location when address cannot be found") {
        val location = googleMapsLocationService.geocode("Random Address")
        location shouldBe null
    }

    test("search should return a list of location") {
        val expectedResponse =
            SearchTextResponse.newBuilder()
                .addAllPlaces(ArrayList())
                .addAllRoutingSummaries(ArrayList())
                .addAllContextualContents(ArrayList())
                .build()
        mockPlaces.addResponse(expectedResponse);

        val locations = googleMapsLocationService.search("Whataburger")
        locations.size shouldBe 10
    }

})
