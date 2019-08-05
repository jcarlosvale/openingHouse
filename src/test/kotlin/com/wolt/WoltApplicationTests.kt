package com.wolt

import com.wolt.api.dtos.BusinessHoursDto
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class WoltApplicationTests {

    @LocalServerPort
    var randomServerPort: Int = 0

    val URL = "/wolt/insertOpeningHours"

    @Test
    fun `Test empty DTO`() {

        val expectedReturn: String =
                "Monday: Closed\n" +
                "Tuesday: Closed\n" +
                "Wednesday: Closed\n" +
                "Thursday: Closed\n" +
                "Friday: Closed\n" +
                "Saturday: Closed\n" +
                "Sunday: Closed"

        val businessHourDTO = BusinessHoursDto(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList())

        val request: HttpEntity<BusinessHoursDto> = HttpEntity(businessHourDTO)

        val restTemplate = RestTemplate()

        val baseUrl = "http://localhost:$randomServerPort$URL"
        val uri = URI(baseUrl)

        val actualResult: ResponseEntity<String> = restTemplate.postForEntity(uri, request, String::class.java)

        assertEquals(HttpStatus.ACCEPTED.value(), actualResult.statusCodeValue)

        assertEquals(expectedReturn, actualResult.body)
    }

    @Test(expected = HttpClientErrorException.UnsupportedMediaType::class)
    fun `Test invalid JSON missing days`() {
        val json: String =
                "{\n" +
                "    \"monday\" : [],\n" +
                "    \"tuesday\" : [],\n" +
                "    \"wednesday\" : [],\n" +
                "    \"thursday\" : [],\n" +
                "    \"friday\" : [],\n" +
                "    \"sunday\" : []\n" +
                "}"


        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val request = HttpEntity(json, headers)

        val restTemplate = RestTemplate()

        val baseUrl = "http://localhost:$randomServerPort$URL"
        val uri = URI(baseUrl)

        restTemplate.postForEntity(uri, request, String::class.java)
    }

}
