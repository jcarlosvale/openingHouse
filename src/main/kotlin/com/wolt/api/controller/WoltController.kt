package com.wolt.api.controller

import com.wolt.api.dtos.BusinessHoursDto
import com.wolt.api.exceptions.ParserException
import com.wolt.api.service.WoltService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.ws.rs.core.MediaType


@RestController
class WoltController
@Autowired
constructor(val woltService: WoltService) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON])
    fun insertOpeningHours(@RequestBody(required = true) businessHoursDto: BusinessHoursDto): ResponseEntity<Any> {
        val result: String =
        try {
            woltService.process(businessHoursDto)
        } catch (e: ParserException) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }  catch (e: ParserException) {
            return ResponseEntity("Unexpected error, contact the support and provide the used request.",
                    HttpStatus.INTERNAL_SERVER_ERROR)
        }

        return ResponseEntity(result, HttpStatus.ACCEPTED)
    }

}