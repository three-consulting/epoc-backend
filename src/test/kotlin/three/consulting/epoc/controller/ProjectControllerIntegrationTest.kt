package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import three.consulting.epoc.utils.jsonPostEntity
import java.net.URI

class ProjectControllerIntegrationTest : ControllerIntegrationTest() {

    @Test
    fun `get project for id returns 200`() {
        val response = restTemplate.getForEntity("/project/1", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `add project returns 200`() {
        val httpEntity = jsonPostEntity("project/validCreation.json")
        val response = restTemplate.postForEntity("/project", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `add project without name returns 400`() {
        val httpEntity = jsonPostEntity("project/invalidCreation.json")
        val response = restTemplate.postForEntity("/project", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `update project returns 200`() {
        val httpEntity = jsonPostEntity("project/validUpdate.json")
        val response = restTemplate.exchange(
            URI("/project"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `update project without name returns 400`() {
        val httpEntity = jsonPostEntity("project/invalidUpdate.json")
        val response = restTemplate.exchange(
            URI("/project"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `delete project returns 204`() {
        val response = restTemplate.exchange(
            URI("/project/1"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete without path id returns 400`() {
        val response = restTemplate.exchange(
            URI("/project"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @Test
    fun `get all project returns 200`() {
        val response = restTemplate.getForEntity("/project", ArrayNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}
