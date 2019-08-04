package com.wolt.api.enums

enum class EventType(val type: String) {
    OPEN("open"),
    CLOSE("close");

    override fun toString(): String {
        return type
    }

    fun isOpenEvent(): Boolean {
        return this == OPEN
    }

    fun isCloseEvent(): Boolean {
        return this == CLOSE
    }
}
