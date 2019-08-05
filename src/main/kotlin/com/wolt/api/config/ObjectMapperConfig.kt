package com.wolt.api.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Necessary to read and write Enums
 */
@Configuration
class ObjectMapperConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
        return mapper
    }
}
