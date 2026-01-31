package dev.lewischan.weatherbot.google.maps.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.web.client.RestClient
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.json.JsonMapper
import java.time.Duration

@Configuration
@EnableConfigurationProperties(GoogleMapsServicesProperties::class)
class GoogleMapsServicesConfiguration {

    @Bean
    fun googleMapsRestClient(
        googleMapsServicesProperties: GoogleMapsServicesProperties
    ): RestClient {
        val snakeCaseJsonMapper = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.setReadTimeout(Duration.ofSeconds(3))
        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(3))

        return RestClient.builder()
            .requestFactory(requestFactory)
            .baseUrl(googleMapsServicesProperties.apiBaseUrl)
            .configureMessageConverters { it.addCustomConverter(JacksonJsonHttpMessageConverter(snakeCaseJsonMapper)) }
            .build()
    }
}
