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

/**
 * Main controller to post the Open Hours
 * Returns HTTP CODE 202 - ACCEPTED in success
 * OR
 * Returns HTTP CODE 400 - In case of any parse error from request
 * Otherwise: 500 - INTERNAL ERROR
 */
@RestController
class WoltController
@Autowired
constructor(val woltService: WoltService) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON])
    fun insertOpeningHours(@RequestBody(required = true) businessHoursDto: BusinessHoursDto): ResponseEntity<Any> {
        val result: String = woltService.process(businessHoursDto)
        return ResponseEntity(result, HttpStatus.ACCEPTED)
    }

}