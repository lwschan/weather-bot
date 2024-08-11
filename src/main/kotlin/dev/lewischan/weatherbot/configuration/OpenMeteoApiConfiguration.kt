package dev.lewischan.weatherbot.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(OpenMeteoApiProperties::class)
class OpenMeteoApiConfiguration {

    @Bean
    fun openMeteoRestClient(openMeteoApiProperties: OpenMeteoApiProperties): RestClient {
        return RestClient.builder()
            .requestFactory(HttpComponentsClientHttpRequestFactory())
            .baseUrl(openMeteoApiProperties.baseUrl)
            .build()
    }
}