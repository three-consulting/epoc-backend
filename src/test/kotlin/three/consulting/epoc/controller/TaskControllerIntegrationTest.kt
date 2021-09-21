package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import three.consulting.epoc.utils.jsonPostEntity
import java.net.URI

class TaskControllerIntegrationTest : ControllerIntegrationTest() {

    @Test
    fun `get task for id returns 200`() {
        val response = restTemplate.getForEntity("/task/1", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `add task returns 200`() {
        val httpEntity = jsonPostEntity("task/validCreation.json")
        val response = restTemplate.postForEntity("/task", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `add task without name returns 400`() {
        val httpEntity = jsonPostEntity("task/invalidCreation.json")
        val response = restTemplate.postForEntity("/task", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `update task returns 200`() {
        val httpEntity = jsonPostEntity("task/validUpdate.json")
        val response = restTemplate.exchange(
            URI("/task"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `update task without name returns 400`() {
        val httpEntity = jsonPostEntity("task/invalidUpdate.json")
        val response = restTemplate.exchange(
            URI("/task"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `delete task returns 204`() {
        val response = restTemplate.exchange(
            URI("/task/1"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete without path id returns 400`() {
        val response = restTemplate.exchange(
            URI("/task"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }
}
