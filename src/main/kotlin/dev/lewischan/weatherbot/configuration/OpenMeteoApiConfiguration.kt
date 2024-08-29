package dev.lewischan.weatherbot.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(OpenMeteoApiProperties::class)
open class OpenMeteoApiConfiguration {

    @Bean
    open fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        val snakeCaseObjectMapper = ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)

        return MappingJackson2HttpMessageConverter(snakeCaseObjectMapper)
    }

    @Bean
    open fun openMeteoRestClient(
        openMeteoApiProperties: OpenMeteoApiProperties,
        mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter
    ): RestClient {
        return RestClient.builder()
            .requestFactory(HttpComponentsClientHttpRequestFactory())
            .baseUrl(openMeteoApiProperties.baseUrl)
            .messageConverters { converters -> converters.add(mappingJackson2HttpMessageConverter) }
            .build()
    }
}