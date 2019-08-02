package com.wolt.api.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import com.wolt.api.enums.HourEventType

data class HourEventDto (
        @JsonProperty(value = "type", required = false )
        val type: HourEventType,

        @JsonProperty(value = "value", required = false )
        val value: Long
)
