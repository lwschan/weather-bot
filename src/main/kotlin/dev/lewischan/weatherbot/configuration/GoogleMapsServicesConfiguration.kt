package dev.lewischan.weatherbot.configuration

import com.google.maps.GeoApiContext
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GoogleMapsServicesProperties::class)
class GoogleMapsServicesConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = ["geoApiContext"])
    fun geoApiContext(
        googleMapsServicesProperties: GoogleMapsServicesProperties
    ): GeoApiContext {
        return GeoApiContext.Builder()
            .apiKey(googleMapsServicesProperties.apiKey)
            .queryRateLimit(googleMapsServicesProperties.queryRateLimit)
            .build()
    }
}