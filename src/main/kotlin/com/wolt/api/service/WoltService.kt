package com.wolt.api.service

import com.wolt.api.converter.BusinessHourConverter
import com.wolt.api.domain.BusinessHours
import com.wolt.api.domain.HourEvent
import com.wolt.api.dtos.BusinessHoursDto
import com.wolt.api.enums.EventType
import com.wolt.api.validators.BusinessHoursValidator
import com.wolt.api.validators.HourRangeValidator
import com.wolt.api.validators.OpenCloseHoursSequenceValidator
import com.wolt.api.validators.WorkingDaySequenceValidator
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

/**
 * Main service class used to process information from Controller and uses the other components: Validator, Converter
 * and the domain objects
 */
@Service
class WoltService {

    fun process(businessHoursDto: BusinessHoursDto) : String {

        val businessHours : BusinessHours = BusinessHourConverter().convert(businessHoursDto)

        parseEvents(businessHours)

        return readableFormat(businessHours)
    }

    private fun parseEvents(businessHours: BusinessHours) {

        validate(businessHours)

        for (dayOfWeek : DayOfWeek in DayOfWeek.values()) {

            if (!businessHours.isWorkingDay(dayOfWeek)) continue

            //fixing the events
            if (businessHours.dayStartsWithEventType(dayOfWeek, EventType.CLOSE)) {
                //swap
                removeFirstElementFromAndAppendTo(businessHours, dayOfWeek, dayOfWeek.minus(1))
            }

            if (businessHours.dayEndsWithEventType(dayOfWeek, EventType.OPEN)) {
                //swap
                removeFirstElementFromAndAppendTo(businessHours, dayOfWeek.plus(1), dayOfWeek)
            }
        }
    }

    private fun validate(businessHours: BusinessHours) {

        val businessHoursValidator: List<BusinessHoursValidator<BusinessHours>> =
                listOf(
                        WorkingDaySequenceValidator(),
                        OpenCloseHoursSequenceValidator(),
                        HourRangeValidator()
                )

        businessHoursValidator.forEach { it.validate(businessHours) }

    }

    private fun readableFormat(businessHours: BusinessHours): String {

        val result : StringBuilder = StringBuilder()

        for (dayOfWeek : DayOfWeek in DayOfWeek.values().sorted()) {

            val dayOfWeekDisplayName: String = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US) + ":"

            var line: String = dayOfWeekDisplayName

            if (!businessHours.isWorkingDay(dayOfWeek)) {

                line = "$line Closed"

            } else {

                for (hourEvent: HourEvent in businessHours.getListOfHourEvent(dayOfWeek)) {

                    if (hourEvent.type == EventType.OPEN) {
                        line = "$line ${hourEvent.getHourFromUnixTimeStampString()} -"
                    } else {
                        line = "$line ${hourEvent.getHourFromUnixTimeStampString()}"

                        if (hourEvent != businessHours.getListOfHourEvent(dayOfWeek).last()) line = "$line,"
                    }

                }
            }

            if (dayOfWeek != DayOfWeek.SUNDAY)
                result.appendln(line)
            else
                result.append(line)
        }

        print(result)

        return result.toString()
    }

    private fun removeFirstElementFromAndAppendTo(businessHours: BusinessHours,
                                                  fromDayOfWeek: DayOfWeek,
                                                  toDayOfWeek: DayOfWeek) {
        val tempHourEvent : HourEvent = businessHours.getListOfHourEvent(fromDayOfWeek).first()
        businessHours.getListOfHourEvent(fromDayOfWeek).remove(tempHourEvent)
        businessHours.getListOfHourEvent(toDayOfWeek).add(tempHourEvent)
    }
}