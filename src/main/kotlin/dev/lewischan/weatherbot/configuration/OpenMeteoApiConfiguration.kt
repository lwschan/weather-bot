package dev.lewischan.weatherbot.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(OpenMeteoApiProperties::class)
class OpenMeteoApiConfiguration {

    @Bean
    fun openMeteoRestClient(
        openMeteoApiProperties: OpenMeteoApiProperties,
        mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter
    ): RestClient {
        val snakeCaseObjectMapper = jacksonObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        return RestClient.builder()
            .requestFactory(HttpComponentsClientHttpRequestFactory())
            .baseUrl(openMeteoApiProperties.baseUrl)
            .messageConverters { converters -> run {
                    converters.removeIf { it is MappingJackson2HttpMessageConverter }
                    converters.add(MappingJackson2HttpMessageConverter(snakeCaseObjectMapper))
            } }
            .build()
    }
}