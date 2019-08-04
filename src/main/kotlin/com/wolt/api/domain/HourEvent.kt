package com.wolt.api.domain

import com.wolt.api.enums.EventType
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class HourEvent (val type: EventType, val value: Long) {

    fun getTimeFromUnixTimeStamp(): LocalTime {
        return LocalDateTime
                .ofInstant(Instant.ofEpochMilli(value * 1000), TimeZone.getTimeZone("GMT").toZoneId())
                .toLocalTime()
    }

    fun getTimeFromUnixTimeStampString(formatStyle: String): String {
        return DateTimeFormatter.ofPattern(formatStyle).format(getTimeFromUnixTimeStamp())
    }

}