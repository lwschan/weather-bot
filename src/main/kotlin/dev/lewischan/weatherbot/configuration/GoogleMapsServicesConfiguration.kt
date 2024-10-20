package dev.lewischan.weatherbot.configuration

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.api.gax.rpc.FixedHeaderProvider
import com.google.auth.ApiKeyCredentials
import com.google.maps.GeoApiContext
import com.google.maps.places.v1.PlacesClient
import com.google.maps.places.v1.PlacesSettings
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GoogleMapsServicesProperties::class)
class GoogleMapsServicesConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun geoApiContext(
        googleMapsServicesProperties: GoogleMapsServicesProperties
    ): GeoApiContext {
        return GeoApiContext.Builder()
            .apiKey(googleMapsServicesProperties.apiKey)
            .queryRateLimit(googleMapsServicesProperties.queryRateLimit)
            .build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun placesClient(googleMapsServicesProperties: GoogleMapsServicesProperties): PlacesClient {
        val fieldMask = listOf(
            "places.id",
            "places.displayName",
            "places.formattedAddress",
            "places.shortFormattedAddress",
            "places.location"
        ).joinToString(",")

        val placesSettings = PlacesSettings.newBuilder()
            .setTransportChannelProvider(PlacesSettings.defaultGrpcTransportProviderBuilder().build())
            .setHeaderProvider(FixedHeaderProvider.create("X-Goog-FieldMask", fieldMask))
            .setCredentialsProvider(FixedCredentialsProvider.create(ApiKeyCredentials.create(googleMapsServicesProperties.apiKey)))
            .build()

        return PlacesClient.create(placesSettings)
    }
}