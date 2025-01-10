package dev.lewischan.weatherbot

import com.github.kotlintelegrambot.Bot
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.google.api.gax.core.NoCredentialsProvider
import com.google.api.gax.grpc.testing.MockServiceHelper
import com.google.maps.GeoApiContext
import com.google.maps.places.v1.MockPlaces
import com.google.maps.places.v1.PlacesClient
import com.google.maps.places.v1.PlacesSettings
import dev.lewischan.weatherbot.configuration.GoogleMapsServicesProperties
import io.mockk.mockk
import io.mockk.spyk
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class IntTestConfiguration {

    @Bean
    fun wireMockServerPort(@Value("\${wiremock.port}") wireMockPort: Int): Int {
        return wireMockPort
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun wireMockServer(wireMockServerPort: Int): WireMockServer {
        return WireMockServer(wireMockServerPort)
    }

    @Bean(initMethod = "initMock")
    fun wireMockServerConfiguration(wireMockServer: WireMockServer): WireMockServerConfiguration {
        return WireMockServerConfiguration(wireMockServer)
    }

    @Bean
    fun geoApiContext(
        googleMapsServicesProperties: GoogleMapsServicesProperties,
        wireMockServerPort: Int
    ): GeoApiContext {
        return GeoApiContext.Builder()
            .apiKey(googleMapsServicesProperties.apiKey)
            .baseUrlOverride("http://localhost:$wireMockServerPort")
            .build()
    }

    @Bean
    fun mockPlacesService(): MockPlaces {
        return MockPlaces()
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun mockPlacesServiceHelper(mockPlacesService: MockPlaces): MockServiceHelper {
        return MockServiceHelper(UUID.randomUUID().toString(), listOf(mockPlacesService))
    }

    @Bean
    fun searchPlacesClient(mockPlacesServiceHelper: MockServiceHelper): PlacesClient {
        val grpcChannelProvider = mockPlacesServiceHelper.createChannelProvider()
        val placesSettings = PlacesSettings.newBuilder()
            .setTransportChannelProvider(grpcChannelProvider)
            .setCredentialsProvider(NoCredentialsProvider.create())
            .build()
        return spyk(PlacesClient.create(placesSettings))
    }

    @Bean
    fun bot(): Bot {
        return mockk<Bot>(relaxed = true)
    }
}