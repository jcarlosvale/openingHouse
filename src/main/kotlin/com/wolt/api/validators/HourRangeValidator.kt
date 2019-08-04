package com.wolt.api.validators

import com.wolt.api.domain.BusinessHours
import com.wolt.api.domain.HourEvent
import com.wolt.api.exceptions.ParserException
import java.time.DayOfWeek

class HourRangeValidator  : BusinessHoursValidator<BusinessHours> {

    override fun validate(businessHours: BusinessHours) {

        for (dayOfWeek : DayOfWeek in DayOfWeek.values()) {

            if (!businessHours.isWorkingDay(dayOfWeek)) continue

            for (hourEvent: HourEvent in businessHours.getListOfHourEvent(dayOfWeek)) {

                if (hourEvent.value !in 0..86399)
                    throw ParserException("Invalid time value [0-86399] in $dayOfWeek.")

            }

        }

    }
}