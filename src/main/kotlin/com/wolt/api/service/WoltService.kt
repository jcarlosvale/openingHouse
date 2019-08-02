package com.wolt.api.service

import com.wolt.api.dtos.HourEventDto
import com.wolt.api.dtos.BusinessHoursDto
import com.wolt.api.enums.HourEventType
import com.wolt.api.exceptions.ParserException
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

@Service
class WoltService {

    //TODO: tentar criar uma CLASSE para o MAPA
    fun process(businessHoursDto: BusinessHoursDto) : String {

        val mapOfBusinessHours: MutableMap<DayOfWeek, MutableList<HourEventDto>> =
                convertToMutableAndSortedMap(businessHoursDto)

        parse(mapOfBusinessHours)

        return readableFormat(mapOfBusinessHours)
    }

    //TODO MUDAR LOCALIZACAO
    private fun getTimeFromUnixTimeStamp(unixTimeStamp: Long): LocalTime {
        return LocalDateTime
                .ofInstant(Instant.ofEpochMilli(unixTimeStamp * 1000), TimeZone.getTimeZone("GMT").toZoneId())
                .toLocalTime()
    }

    private fun readableFormat(mapOfBusinessHours: Map<DayOfWeek, List<HourEventDto>>): String {

        val result : StringBuilder = StringBuilder()

        for (dayOfWeek : DayOfWeek in DayOfWeek.values().sorted()) {

            val dayOfWeekDisplayName: String = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US) + ":"

            if (!isWorkingDay(mapOfBusinessHours, dayOfWeek)) {
                result.appendln("$dayOfWeekDisplayName Closed")
            } else {
                var line = dayOfWeekDisplayName
                for (hourEvent in mapOfBusinessHours.getValue(dayOfWeek)) {
                    if (hourEvent.type.isOpenHourEvent()) {
                        line = "$line ${getTimeFromUnixTimeStamp(hourEvent.value)} - "
                    }
                    else {
                        line = "$line ${getTimeFromUnixTimeStamp(hourEvent.value)}"
                        if (hourEvent != mapOfBusinessHours.getValue(dayOfWeek).last()) line = "$line,"
                    }

                }
                result.appendln(line)
            }
        }

        print(result)
        return result.toString()
    }

    //TODO Logging
    protected fun parse(mapOfBusinessHours: MutableMap<DayOfWeek, MutableList<HourEventDto>>) {
        for (dayOfWeek : DayOfWeek in DayOfWeek.values()) {

            //validações
            if (!isWorkingDay(mapOfBusinessHours, dayOfWeek)) continue

            //fixing the elements
            if (firstHourEventTypeOfDay(mapOfBusinessHours, dayOfWeek).isCloseHourEvent()) {
                val previousDayOfWeek: DayOfWeek = dayOfWeek.minus(1)

                if (!isWorkingDay(mapOfBusinessHours, previousDayOfWeek))
                    throw ParserException("${previousDayOfWeek} must be a working day.")

                if (lastHourEventTypeOfDay(mapOfBusinessHours, previousDayOfWeek).isCloseHourEvent()) {
                    throw ParserException("The last hour event type of ${previousDayOfWeek} must be an OPEN event.")
                }

                //swap
                removeFirstElementFromAndAppendTo(mapOfBusinessHours,dayOfWeek,previousDayOfWeek)
            }

            if (lastHourEventTypeOfDay(mapOfBusinessHours, dayOfWeek).isOpenHourEvent()) {
                val nextDayOfWeek: DayOfWeek = dayOfWeek.plus(1)

                if (!isWorkingDay(mapOfBusinessHours, nextDayOfWeek))
                    throw ParserException("${nextDayOfWeek} must be a working day.")

                if (firstHourEventTypeOfDay(mapOfBusinessHours, nextDayOfWeek).isOpenHourEvent()) {
                    throw ParserException("The first hour event type of ${nextDayOfWeek} must be an CLOSE event.")
                }
                //swap
                removeFirstElementFromAndAppendTo(mapOfBusinessHours,nextDayOfWeek, dayOfWeek)
            }

            //TODO Verificando os opens and close and time
            var previousHourEventType: HourEventType = firstHourEventTypeOfDay(mapOfBusinessHours, dayOfWeek)
            val listOfHourEventDto = mapOfBusinessHours[dayOfWeek]

            for (index in 1 until (listOfHourEventDto!!.size)) {

                val hourEvent = mapOfBusinessHours[dayOfWeek]!![index]

                //TODO time validation
                if (hourEvent.value !in 0..86399)
                    throw ParserException("Invalid time value [0-86399] in ${dayOfWeek}.")

                if (previousHourEventType == hourEvent.type)
                    throw ParserException("Missing correct OPEN/CLOSE sequence in ${dayOfWeek}.")

                previousHourEventType = hourEvent.type
            }
        }
    }

    private fun removeFirstElementFromAndAppendTo(mapOfBusinessHours: MutableMap<DayOfWeek, MutableList<HourEventDto>>,
                                                  fromDayOfWeek: DayOfWeek,
                                                  toDayOfWeek: DayOfWeek) {
        val tempHourEventDto: HourEventDto = mapOfBusinessHours[fromDayOfWeek]!!.first()
        mapOfBusinessHours[fromDayOfWeek]!!.remove(tempHourEventDto)
        mapOfBusinessHours[toDayOfWeek]!!.add(tempHourEventDto)
    }

    private fun firstHourEventTypeOfDay(mapOfBusinessHours: Map<DayOfWeek, List<HourEventDto>>,
                                        dayOfWeek: DayOfWeek): HourEventType {
        return mapOfBusinessHours.getValue(dayOfWeek).first().type
    }

    private fun lastHourEventTypeOfDay(mapOfBusinessHours: Map<DayOfWeek, List<HourEventDto>>,
                                       dayOfWeek: DayOfWeek): HourEventType {
        return mapOfBusinessHours.getValue(dayOfWeek).last().type
    }

    private fun isWorkingDay(mapOfBusinessHours: Map<DayOfWeek, List<HourEventDto>>,
                             dayOfWeek: DayOfWeek): Boolean {
        return mapOfBusinessHours[dayOfWeek]?.isNotEmpty() ?: false
    }

    protected fun convertToMutableAndSortedMap(businessHoursDto: BusinessHoursDto) : MutableMap<DayOfWeek, MutableList<HourEventDto>>{
        val mapWeekBusinessHour : MutableMap<DayOfWeek, MutableList<HourEventDto>> = mutableMapOf()

        mapWeekBusinessHour[DayOfWeek.MONDAY] = convertListToSortedMutableList(businessHoursDto.monday)
        mapWeekBusinessHour[DayOfWeek.TUESDAY] = convertListToSortedMutableList(businessHoursDto.tuesday)
        mapWeekBusinessHour[DayOfWeek.WEDNESDAY] = convertListToSortedMutableList(businessHoursDto.wednesday)
        mapWeekBusinessHour[DayOfWeek.THURSDAY] = convertListToSortedMutableList(businessHoursDto.thursday)
        mapWeekBusinessHour[DayOfWeek.FRIDAY] = convertListToSortedMutableList(businessHoursDto.friday)
        mapWeekBusinessHour[DayOfWeek.SATURDAY] = convertListToSortedMutableList(businessHoursDto.saturday)
        mapWeekBusinessHour[DayOfWeek.SUNDAY] = convertListToSortedMutableList(businessHoursDto.sunday)

        return mapWeekBusinessHour
    }

    private fun convertListToSortedMutableList(listOfHourEventDto: List<HourEventDto>): MutableList<HourEventDto> {
        val mutableListOfHourEventDto: MutableList<HourEventDto> = listOfHourEventDto.toMutableList()
        mutableListOfHourEventDto.sortBy { it.value }
        return mutableListOfHourEventDto
    }
}