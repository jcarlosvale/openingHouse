package com.wolt.api.enums

enum class OpenHourTypeEnum(val type: String) {
    openValue("open"),
    closeValue("close");

    override fun toString(): String {
        return type
    }
}
