package com.wolt.api.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import com.wolt.api.enums.EventType

/**
 * DTO class for HourEvent, receive data from Controller
 */
data class HourEventDto (
        @JsonProperty(value = "type", required = false )
        val type: EventType,

        @JsonProperty(value = "value", required = false )
        val value: Long
)
