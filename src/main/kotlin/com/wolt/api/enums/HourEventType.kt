package com.wolt.api.enums

enum class HourEventType(val type: String) {
    openValue("open"),
    closeValue("close");

    override fun toString(): String {
        return type
    }

    fun isOpenHourEvent(): Boolean {
        return this == openValue
    }

    fun isCloseHourEvent(): Boolean {
        return this == closeValue
    }
}
