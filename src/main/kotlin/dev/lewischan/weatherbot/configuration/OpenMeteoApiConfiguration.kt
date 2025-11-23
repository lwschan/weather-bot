package dev.lewischan.weatherbot.configuration

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
@EnableConfigurationProperties(OpenMeteoApiProperties::class)
class OpenMeteoApiConfiguration(private val jsonMapper: JsonMapper) {

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
        val snakeCaseJsonMapper = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.setReadTimeout(Duration.ofSeconds(3))
        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(3))

        return RestClient.builder()
            .requestFactory(requestFactory)
            .baseUrl(baseUrl)
            .configureMessageConverters {
                it -> it.addCustomConverter(JacksonJsonHttpMessageConverter(snakeCaseJsonMapper))
            }
            .build()
    }
}