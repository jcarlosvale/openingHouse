package com.wolt.api

import com.wolt.api.exceptions.ParserException
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class MainExceptionHandler {

    private val log: KLogger = KotlinLogging.logger {}

    @ExceptionHandler(HttpMessageConversionException::class)
    fun handleHttpMessageConversionException(e: HttpMessageConversionException): ResponseEntity<String> {
        log.info {e.message }
        return ResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

    @ExceptionHandler(ParserException::class)
    fun handleParserException(e: ParserException): ResponseEntity<String> {
        log.info {e.message }
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<String> {
        log.error {e.message }
        return ResponseEntity("Unexpected error, contact the support and provide the used request.",HttpStatus.INTERNAL_SERVER_ERROR)
    }
}