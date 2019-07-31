package com.wolt.api.controller

import com.wolt.api.dtos.BusinessHoursDto
import com.wolt.api.service.WoltService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.ws.rs.core.MediaType


@RestController
class WoltController
@Autowired
constructor(val woltService: WoltService) {

    @GetMapping
    fun test(): String {
        return "TEST"
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON])
    fun insertOpeningHours(@RequestBody(required = true) businessHoursDto: BusinessHoursDto): ResponseEntity<Any> {
        woltService.process(businessHoursDto)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }
}