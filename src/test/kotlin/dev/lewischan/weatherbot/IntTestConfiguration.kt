package dev.lewischan.weatherbot

import com.github.tomakehurst.wiremock.WireMockServer
import com.google.maps.GeoApiContext
import dev.lewischan.weatherbot.configuration.GoogleMapsServicesProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.test.util.TestSocketUtils

@Configuration
class IntTestConfiguration {

    private val wireMockServerPort: Int = TestSocketUtils.findAvailableTcpPort()

    @Bean
    fun wireMockServer(): WireMockServer {
        return WireMockServer(wireMockServerPort)
    }

    @Bean
    @Primary
    fun geoApiContext(googleMapsServicesProperties: GoogleMapsServicesProperties): GeoApiContext {
        return GeoApiContext.Builder()
            .apiKey(googleMapsServicesProperties.apiKey)
            .baseUrlOverride("http://localhost:$wireMockServerPort")
            .build()
    }
}