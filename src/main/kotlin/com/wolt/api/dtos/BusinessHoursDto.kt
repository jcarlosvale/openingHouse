package com.wolt.api.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class BusinessHoursDto (

        @JsonProperty(value = "monday", required = false )
        val monday: List<HourEventDto>,

        @JsonProperty(value = "tuesday", required = false )
        val tuesday: List<HourEventDto>,

        @JsonProperty(value = "wednesday", required = false )
        val wednesday: List<HourEventDto>,

        @JsonProperty(value = "thursday", required = false )
        val thursday: List<HourEventDto>,

        @JsonProperty(value = "friday", required = false )
        val friday: List<HourEventDto>,

        @JsonProperty(value = "saturday", required = false )
        val saturday: List<HourEventDto>,

        @JsonProperty(value = "sunday", required = false )
        val sunday: List<HourEventDto>
)
