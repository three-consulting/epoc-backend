package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import three.consulting.epoc.utils.jsonPostEntity
import java.net.URI

class TimesheetEntryControllerIntegrationTest : ControllerIntegrationTest() {
    @Test
    fun `get timesheetEntry for id returns 200`() {
        val response = restTemplate.getForEntity("/timesheet-entry/1", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `get employeeFlex for valid email returns 200`() {
        val response = restTemplate.getForEntity("/timesheet-entry/flex?email=testi@tekija.fi", Float::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `get employeeFlex without email returns 400`() {
        val response = restTemplate.getForEntity("/timesheet-entry/flex", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `add timesheetEntries returns 200`() {
        val httpEntity = jsonPostEntity("timesheetEntry/validCreateMany.json")
        val response = restTemplate.postForEntity("/timesheet-entry", httpEntity, ArrayNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `add timesheetEntry without name returns 400`() {
        val httpEntity = jsonPostEntity("timesheetEntry/invalidNameCreation.json")
        val response = restTemplate.postForEntity("/timesheet-entry", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `update timesheetEntries returns 200`() {
        val httpEntity = jsonPostEntity("timesheetEntry/validUpdate.json")
        val response =
            restTemplate.exchange(
                URI("/timesheet-entry"),
                HttpMethod.PUT,
                httpEntity,
                ArrayNode::class.java
            )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete timesheetEntries returns 204`() {
        val httpEntity = jsonPostEntity("timesheetEntry/deleteEntries.json")
        val response =
            restTemplate.exchange(
                URI("/timesheet-entry"),
                HttpMethod.DELETE,
                httpEntity,
                ArrayNode::class.java
            )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete without request body returns 400`() {
        val response =
            restTemplate.exchange(
                URI("/timesheet-entry"),
                HttpMethod.DELETE,
                null,
                ObjectNode::class.java
            )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `get timesheet entries with timesheetId returns 200`() {
        val response = restTemplate.getForEntity("/timesheet-entry?timesheetId=1", ArrayNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `getting timesheet with all request parameters returns 200`() {
        val response =
            restTemplate.getForEntity(
                "/timesheet-entry?email=testi@tekija.fi&startDate=2022-01-01&endDate=2022-04-01",
                ArrayNode::class.java
            )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `get timesheet entries csv export with right request params returns 200`() {
        val response = restTemplate.getForEntity("/timesheet-entry/csv-export?startDate=2022-01-01&endDate=2023-01-01", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `get timesheet entries csv export with missing request param endDate returns 400`() {
        val response = restTemplate.getForEntity("/timesheet-entry/csv-export?startDate=2022-01-01", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
