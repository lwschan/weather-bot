package dev.lewischan.weatherbot.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.web.client.RestClient
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.json.JsonMapper
import java.time.Duration

@Configuration
@EnableConfigurationProperties(PirateWeatherApiProperties::class)
class PirateWeatherApiConfiguration {

    @Bean
    fun pirateWeatherRestClient(
        pirateWeatherApiProperties: PirateWeatherApiProperties
    ): RestClient {
        val jsonMapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.setReadTimeout(Duration.ofSeconds(10))
        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(3))

        return RestClient.builder()
            .requestFactory(requestFactory)
            .baseUrl(pirateWeatherApiProperties.baseUrl)
            .configureMessageConverters {
                it.addCustomConverter(JacksonJsonHttpMessageConverter(jsonMapper))
            }
            .build()
    }
}
