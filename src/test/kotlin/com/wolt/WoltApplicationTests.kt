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

        val baseUrl = "http://localhost:$randomServerPort$URL"

        val actualResult: ResponseEntity<String> = RestTemplate().postForEntity(URI(baseUrl), request, String::class.java)

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

        val request: HttpEntity<String> = HttpEntity(json, headers)

        val baseUrl = "http://localhost:$randomServerPort$URL"

        RestTemplate().postForEntity(URI(baseUrl), request, String::class.java)
    }

    @Test
    fun `Test happy process`() {

        val expectedReturn: String =
                "Monday: 10 AM - 8 PM\n" +
                "Tuesday: Closed\n" +
                "Wednesday: Closed\n" +
                "Thursday: Closed\n" +
                "Friday: 10 AM - 6 PM\n" +
                "Saturday: 6 PM - 1 AM\n" +
                "Sunday: 9 AM - 11 AM, 4 PM - 11 PM"

        val json: String =
                "{\n" +
                "    \"monday\" : [\n" +
                "    {\n" +
                "    \"type\" : \"open\",\n" +
                "    \"value\" : 36000 \n" +
                "},\n" +
                "{\n" +
                "    \"type\" : \"close\",\n" +
                "    \"value\" : 72000\n" +
                "}\n" +
                "],\n" +
                "\"tuesday\" : [],\n" +
                "\"wednesday\" : [],\n" +
                "\"thursday\" : [],\n" +
                "\"friday\" : [\n" +
                "{\n" +
                "   \"type\" : \"open\",\n" +
                "   \"value\" : 36000 \n" +
                "},\n" +
                "{\n" +
                "   \"type\" : \"close\",\n" +
                "   \"value\" : 64800\n" +
                "}],\n" +
                "\"saturday\" : [\n" +
                "{\n" +
                "   \"type\" : \"open\", \n" +
                "   \"value\" : 64800\n" +
                "}\n" +
                "],\n" +
                "\"sunday\" : [\n" +
                "{\n" +
                "   \"type\" : \"close\",\n" +
                "   \"value\" : 3600\n" +
                "},\n" +
                "{\n" +
                "   \"type\" : \"open\",\n" +
                "   \"value\" : 32400 \n" +
                "},\n" +
                "{\n" +
                "   \"type\" : \"close\", \n" +
                "   \"value\" : 39600\n" +
                "},\n" +
                "{\n" +
                "   \"type\" : \"open\",\n" +
                "   \"value\" : 57600 \n" +
                "},\n" +
                "{\n" +
                "   \"type\" : \"close\", \n" +
                "   \"value\" : 82800\n" +
                "}]\n" +
                "}"

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val request: HttpEntity<String> = HttpEntity(json, headers)

        val baseUrl = "http://localhost:$randomServerPort$URL"

        val actualResult: ResponseEntity<String> = RestTemplate().postForEntity(URI(baseUrl), request, String::class.java)

        assertEquals(HttpStatus.ACCEPTED.value(), actualResult.statusCodeValue)

        assertEquals(expectedReturn, actualResult.body)
    }

    @Test(expected = HttpClientErrorException.BadRequest::class)
    fun `Test 400 http code return by invalid hour`() {

        val json: String =
                "{\n" +
                "\"monday\" : [],\n" +
                "\"tuesday\" : [],\n" +
                "\"wednesday\" : [],\n" +
                "\"thursday\" : [],\n" +
                "\"friday\" : [],\n" +
                "\"saturday\" : [\n" +
                "{\n" +
                "   \"type\" : \"open\", \n" +
                "   \"value\" : 86400\n" +
                "}\n" +
                "],\n" +
                "    \"sunday\" : []\n" +
                "}"

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val request: HttpEntity<String> = HttpEntity(json, headers)

        val baseUrl = "http://localhost:$randomServerPort$URL"

        RestTemplate().postForEntity(URI(baseUrl), request, String::class.java)
    }

    @Test(expected = HttpClientErrorException.BadRequest::class)
    fun `Test 400 http code return by open close wrong sequence`() {

        val json: String =
                "{\n" +
                        "\"monday\" : [],\n" +
                        "\"tuesday\" : [],\n" +
                        "\"wednesday\" : [],\n" +
                        "\"thursday\" : [],\n" +
                        "\"friday\" : [],\n" +
                        "\"saturday\" : [\n" +
                        "{\n" +
                        "   \"type\" : \"open\", \n" +
                        "   \"value\" : 36000\n" +
                        "},\n" +
                        "{\n" +
                        "   \"type\" : \"open\", \n" +
                        "   \"value\" : 64800\n" +
                        "}\n" +
                        "],\n" +
                        "    \"sunday\" : []\n" +
                        "}"

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val request: HttpEntity<String> = HttpEntity(json, headers)

        val baseUrl = "http://localhost:$randomServerPort$URL"

        RestTemplate().postForEntity(URI(baseUrl), request, String::class.java)
    }

    @Test(expected = HttpClientErrorException.BadRequest::class)
    fun `Test 400 http code return by missing working day`() {

        val json: String =
                "{\n" +
                        "\"monday\" : [],\n" +
                        "\"tuesday\" : [],\n" +
                        "\"wednesday\" : [],\n" +
                        "\"thursday\" : [],\n" +
                        "\"friday\" : [],\n" +
                        "\"saturday\" : [\n" +
                        "{\n" +
                        "   \"type\" : \"open\", \n" +
                        "   \"value\" : 36000\n" +
                        "}\n" +
                        "],\n" +
                        "    \"sunday\" : []\n" +
                        "}"

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val request: HttpEntity<String> = HttpEntity(json, headers)

        val baseUrl = "http://localhost:$randomServerPort$URL"

        RestTemplate().postForEntity(URI(baseUrl), request, String::class.java)
    }



}
