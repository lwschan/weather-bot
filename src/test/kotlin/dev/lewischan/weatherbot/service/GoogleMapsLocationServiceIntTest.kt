package dev.lewischan.weatherbot.service

import com.google.maps.places.v1.MockPlaces
import com.google.maps.places.v1.Place
import com.google.maps.places.v1.PlacesClient
import com.google.maps.places.v1.SearchTextResponse
import com.google.type.LatLng
import com.google.type.LocalizedText
import dev.lewischan.weatherbot.UseBaseIntTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.verify

@UseBaseIntTest
class GoogleMapsLocationServiceIntTest(
    private val googleMapsLocationService: GoogleMapsLocationService,
    private val mockPlaces: MockPlaces,
    private val searchPlacesClient: PlacesClient
) : FunSpec({

    afterEach {
        mockPlaces.reset()
    }

    test("geocode should convert the address to a location") {
        val location = googleMapsLocationService.geocode("Stamford Bridge, London")
        location shouldNotBe null
        location!!.address shouldBe "Stamford Bridge, York YO41, UK"
        location.latitude shouldBe 53.990129
        location.longitude shouldBe -0.9140249
    }

    test("geocode should return null location when address cannot be found") {
        val location = googleMapsLocationService.geocode("Random Address")
        location shouldBe null
    }

    test("search should be made with the right query") {
        val expectedResponse = SearchTextResponse.newBuilder()
            .addAllPlaces(emptyList())
            .addAllRoutingSummaries(emptyList())
            .addAllContextualContents(emptyList())
            .build()
        mockPlaces.addResponse(expectedResponse)

        val locations = googleMapsLocationService.search("Jewel Changi Airport")

        verify(exactly = 1) {
            searchPlacesClient.searchText(match { it.textQuery == "Jewel Changi Airport" })
        }
        locations.size shouldBe 0
    }

    test("search should convert Places to Location") {
        val expectedPlace = Place.newBuilder()
            .setDisplayName(LocalizedText.newBuilder().setText("Jewel Changi Airport"))
            .setFormattedAddress("78 Airport Boulevard, Singapore 819666")
            .setLocation(LatLng.newBuilder().setLatitude(1.3602148).setLongitude(103.9871849).build())
            .build()
        val expectedResponse = SearchTextResponse.newBuilder()
            .addAllPlaces(listOf(expectedPlace))
            .addAllRoutingSummaries(emptyList())
            .addAllContextualContents(emptyList())
            .build()
        mockPlaces.addResponse(expectedResponse)

        val locations = googleMapsLocationService.search("Jewel Changi Airport")

        locations.size shouldBe 1
        locations[0].address shouldBe "78 Airport Boulevard, Singapore 819666"
        locations[0].latitude shouldBe 1.3602148
        locations[0].longitude shouldBe 103.9871849
    }

})
