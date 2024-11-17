package dev.lewischan.weatherbot.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kotlintelegrambot.entities.Update
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class ObjectMapperConfiguration {

    private val snakeCaseObjectMapper = jacksonObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)

    @Bean
    fun mappingJackson2HttpMessageConverter(
        objectMapper: ObjectMapper
    ): MappingJackson2HttpMessageConverter {
        val mappingJackson2HttpMessageConverter = MappingJackson2HttpMessageConverter();

        mappingJackson2HttpMessageConverter.objectMapper = objectMapper

        mappingJackson2HttpMessageConverter.registerObjectMappersForType(
            Update::class.java,
            {
                it.put(MediaType.APPLICATION_JSON, snakeCaseObjectMapper)
            }
        )

        return mappingJackson2HttpMessageConverter
    }
}