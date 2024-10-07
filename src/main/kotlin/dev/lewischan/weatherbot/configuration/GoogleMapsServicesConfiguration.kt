package dev.lewischan.weatherbot.configuration

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.api.gax.grpc.GrpcTransportChannel
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider
import com.google.api.gax.rpc.FixedHeaderProvider
import com.google.auth.ApiKeyCredentials
import com.google.maps.GeoApiContext
import com.google.maps.places.v1.PlacesClient
import com.google.maps.places.v1.PlacesSettings
import dev.lewischan.weatherbot.service.GoogleMapsLocationService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GoogleMapsServicesProperties::class)
class GoogleMapsServicesConfiguration {

    companion object {
        val PLACES_API_FIELD_MASK = listOf(
            "places.id",
            "places.displayName",
            "places.formattedAddress",
            "places.shortFormattedAddress",
            "places.location"
        )
    }

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

    @Bean
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