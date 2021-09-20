package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import three.consulting.epoc.utils.jsonPostEntity
import java.net.URI

class EmployeeControllerIntegrationTest : ControllerIntegrationTest() {

    @Test
    fun `get employee for id returns 200`() {
        val response = restTemplate.getForEntity("/employee/1", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `add employee returns 200`() {
        val httpEntity = jsonPostEntity("employee/validCreation.json")
        val response = restTemplate.postForEntity("/employee", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `add employee without name returns 400`() {
        val httpEntity = jsonPostEntity("employee/invalidCreation.json")
        val response = restTemplate.postForEntity("/employee", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `update employee returns 200`() {
        val httpEntity = jsonPostEntity("employee/validUpdate.json")
        val response = restTemplate.exchange(
            URI("/employee"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `update employee without name returns 400`() {
        val httpEntity = jsonPostEntity("employee/invalidUpdate.json")
        val response = restTemplate.exchange(
            URI("/employee"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `delete employee returns 204`() {
        val response = restTemplate.exchange(
            URI("/employee/1"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete without path id returns 400`() {
        val response = restTemplate.exchange(
            URI("/employee"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }
}
