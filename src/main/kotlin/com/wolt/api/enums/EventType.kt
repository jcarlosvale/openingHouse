package com.wolt.api.enums

/**
 * Represents the types of one hour event: OPEN or CLOSE
 */
enum class EventType(val type: String) {
    OPEN("open"),
    CLOSE("close");

    override fun toString(): String {
        return type
    }
}
