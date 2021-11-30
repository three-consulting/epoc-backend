package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import three.consulting.epoc.utils.jsonPostEntity
import java.net.URI

class TimesheetControllerIntegrationTest : ControllerIntegrationTest() {

    @Test
    fun `get timesheet for id returns 200`() {
        val response = restTemplate.getForEntity("/timesheet/1", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `add timesheet returns 200`() {
        val httpEntity = jsonPostEntity("timesheet/validCreation.json")
        val response = restTemplate.postForEntity("/timesheet", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `add timesheet without name returns 400`() {
        val httpEntity = jsonPostEntity("timesheet/invalidNameCreation.json")
        val response = restTemplate.postForEntity("/timesheet", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `add timesheet with invalid allocation returns 400`() {
        val httpEntity = jsonPostEntity("timesheet/invalidAllocationCreation.json")
        val response = restTemplate.postForEntity("/timesheet", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `update timesheet returns 200`() {
        val httpEntity = jsonPostEntity("timesheet/validUpdate.json")
        val response = restTemplate.exchange(
            URI("/timesheet"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `update timesheet without name returns 400`() {
        val httpEntity = jsonPostEntity("timesheet/invalidUpdate.json")
        val response = restTemplate.exchange(
            URI("/timesheet"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `delete timesheet returns 204`() {
        val response = restTemplate.exchange(
            URI("/timesheet/1"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete without path id returns 400`() {
        val response = restTemplate.exchange(
            URI("/timesheet"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @Test
    fun `query for project id 1 returns 200`() {
        val response = restTemplate.getForEntity("/timesheet?projectId=1", ArrayNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `query without project id returns bad request`() {
        val response = restTemplate.getForEntity("/timesheet", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
