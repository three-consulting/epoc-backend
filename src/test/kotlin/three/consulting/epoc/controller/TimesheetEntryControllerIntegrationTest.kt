package three.consulting.epoc.controller

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
    fun `add timesheetEntry returns 200`() {
        val httpEntity = jsonPostEntity("timesheetEntry/validCreation.json")
        val response = restTemplate.postForEntity("/timesheet-entry", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `add timesheetEntry without name returns 400`() {
        val httpEntity = jsonPostEntity("timesheetEntry/invalidNameCreation.json")
        val response = restTemplate.postForEntity("/timesheet-entry", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `add timesheetEntry with invalid allocation returns 400`() {
        val httpEntity = jsonPostEntity("timesheetEntry/invalidAllocationCreation.json")
        val response = restTemplate.postForEntity("/timesheet-entry", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `update timesheetEntry returns 200`() {
        val httpEntity = jsonPostEntity("timesheetEntry/validUpdate.json")
        val response = restTemplate.exchange(
            URI("/timesheet-entry"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `update timesheetEntry without name returns 400`() {
        val httpEntity = jsonPostEntity("timesheetEntry/invalidUpdate.json")
        val response = restTemplate.exchange(
            URI("/timesheet-entry"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `delete timesheetEntry returns 204`() {
        val response = restTemplate.exchange(
            URI("/timesheet-entry/1"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete without path id returns 400`() {
        val response = restTemplate.exchange(
            URI("/timesheet-entry"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }
}