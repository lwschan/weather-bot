package dev.lewischan.weatherbot

import com.github.tomakehurst.wiremock.WireMockServer
import com.google.maps.GeoApiContext
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

    @Bean
    fun openMeteoRestClient(): RestClient {
        return OpenMeteoApiConfiguration().openMeteoRestClient(
            OpenMeteoApiProperties("http://localhost:$wireMockServerPort")
        )
    }
}