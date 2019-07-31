package com.wolt.api.service

import com.wolt.api.dtos.HourEventDto
import com.wolt.api.dtos.BusinessHoursDto
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class WoltService {

    fun process(businessHoursDto: BusinessHoursDto):String {
        val readableOpeningHours : StringBuilder = StringBuilder()
        readableOpeningHours.append(process(businessHoursDto.monday, "Monday"))
        readableOpeningHours.append(process(businessHoursDto.tuesday, "Tuesday"))
        readableOpeningHours.append(process(businessHoursDto.wednesday, "Wednesday"))
        readableOpeningHours.append(process(businessHoursDto.thursday, "Thursday"))
        readableOpeningHours.append(process(businessHoursDto.friday, "Friday"))
        readableOpeningHours.append(process(businessHoursDto.saturday, "Saturday"))
        readableOpeningHours.append(process(businessHoursDto.sunday, "Sunday"))
        return readableOpeningHours.toString()
    }

    //TODO: is it possible to add the day of Week in the object DTO ?
    private fun process(openingHoursEventDto: List<HourEventDto>, dayOfWeek: String): String {
        if (openingHoursEventDto.isEmpty()) {
            return "$dayOfWeek: Closed"
        }
       /* for (openHour in openingHoursEventDto) {
            val localTime : LocalTime = LocalTime.
            println(" ${openHour.type} - )
        }*/
        return " "
    }

}