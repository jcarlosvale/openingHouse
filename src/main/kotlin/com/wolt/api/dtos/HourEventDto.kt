package com.wolt.api.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import com.wolt.api.enums.OpenHourTypeEnum

data class HourEventDto (
        @JsonProperty(value = "type", required = false )
        val type: OpenHourTypeEnum,

        @JsonProperty(value = "value", required = false )
        val value: Long
)
