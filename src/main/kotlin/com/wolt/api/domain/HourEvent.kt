package com.wolt.api.domain

import com.wolt.api.enums.EventType
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * Domain object for HourEvent used in the Processing Logic
 * Represents the hour when one restaurant OPEN or CLOSE
 */

class HourEvent (val type: EventType, val value: Long) {

    //Default format 12-hour clock format
    fun getHourFromUnixTimeStampString(formatStyle: String = "h a"): String {

        val localTime: LocalTime =
                LocalDateTime
                .ofInstant(Instant.ofEpochMilli(value * 1000), TimeZone.getTimeZone("GMT").toZoneId())
                .toLocalTime()

        return DateTimeFormatter.ofPattern(formatStyle).format(localTime).toUpperCase()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HourEvent

        if (type != other.type) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString(): String {
        return "HourEvent(type=$type, value=$value)"
    }

}