package dev.lewischan.weatherbot.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient
import java.time.Duration

@Configuration
@EnableConfigurationProperties(OpenMeteoApiProperties::class)
class OpenMeteoApiConfiguration {

    @Bean
    fun openMeteoWeatherRestClient(
        openMeteoApiProperties: OpenMeteoApiProperties
    ): RestClient {
        return createRestClient(openMeteoApiProperties.weatherApiBaseUrl)
    }

    @Bean
    fun openMeteoAirQualityRestClient(
        openMeteoApiProperties: OpenMeteoApiProperties
    ): RestClient {
        return createRestClient(openMeteoApiProperties.airQualityApiBaseUrl)
    }

    private fun createRestClient(baseUrl: String): RestClient {
        val snakeCaseObjectMapper = jacksonObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(JavaTimeModule())

        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.setReadTimeout(Duration.ofSeconds(3))
        requestFactory.setConnectTimeout(Duration.ofSeconds(3))
        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(3))

        return RestClient.builder()
            .requestFactory(requestFactory)
            .baseUrl(baseUrl)
            .messageConverters { converters -> run {
                converters.removeIf { it is MappingJackson2HttpMessageConverter }
                converters.add(MappingJackson2HttpMessageConverter(snakeCaseObjectMapper))
            } }
            .build()
    }
}