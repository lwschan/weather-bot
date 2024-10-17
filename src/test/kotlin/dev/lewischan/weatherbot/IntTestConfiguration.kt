package dev.lewischan.weatherbot

import com.github.tomakehurst.wiremock.WireMockServer
import com.google.api.gax.core.NoCredentialsProvider
import com.google.api.gax.grpc.testing.MockServiceHelper
import com.google.maps.GeoApiContext
import com.google.maps.places.v1.MockPlaces
import com.google.maps.places.v1.MockPlacesImpl
import com.google.maps.places.v1.PlacesClient
import com.google.maps.places.v1.PlacesSettings
import dev.lewischan.weatherbot.configuration.GoogleMapsServicesProperties
import dev.lewischan.weatherbot.configuration.OpenMeteoApiConfiguration
import dev.lewischan.weatherbot.configuration.OpenMeteoApiProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.test.util.TestSocketUtils
import org.springframework.web.client.RestClient
import java.util.*

@Configuration
class IntTestConfiguration {

    private val wireMockServerPort: Int = TestSocketUtils.findAvailableTcpPort()

    @Bean
    fun wireMockServer(): WireMockServer {
        return WireMockServer(wireMockServerPort)
    }

    @Bean
    fun geoApiContext(googleMapsServicesProperties: GoogleMapsServicesProperties): GeoApiContext {
        return GeoApiContext.Builder()
            .apiKey(googleMapsServicesProperties.apiKey)
            .baseUrlOverride("http://localhost:$wireMockServerPort")
            .build()
    }

//    @Bean
//    fun mockPlacesService(): MockPlaces {
//        val sss = MockPlacesImpl()
//        return MockPlaces()
//    }

    @Bean
    fun mockPlacesServiceHelper(mockPlacesService: MockPlaces?): MockServiceHelper {
        return MockServiceHelper(UUID.randomUUID().toString(), listOf(mockPlacesService))
    }

    @Bean
    fun placesClient(mockPlacesServiceHelper: MockServiceHelper): PlacesClient {
        val grpcChannelProvider = mockPlacesServiceHelper.createChannelProvider()
        val placesSettings = PlacesSettings.newBuilder()
            .setTransportChannelProvider(grpcChannelProvider)
            .setCredentialsProvider(NoCredentialsProvider.create())
            .build()
        return PlacesClient.create(placesSettings)
    }

    @Bean
    fun openMeteoRestClient(): RestClient {
        return OpenMeteoApiConfiguration().openMeteoRestClient(
            OpenMeteoApiProperties("http://localhost:$wireMockServerPort")
        )
    }
}