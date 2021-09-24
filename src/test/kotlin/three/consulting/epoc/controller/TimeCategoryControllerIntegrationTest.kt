package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import three.consulting.epoc.utils.jsonPostEntity
import java.net.URI

class TimeCategoryControllerIntegrationTest : ControllerIntegrationTest() {

    @Test
    fun `get timeCategory for id returns 200`() {
        val response = restTemplate.getForEntity("/time-category/1", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `add timeCategory returns 200`() {
        val httpEntity = jsonPostEntity("timeCategory/validCreation.json")
        val response = restTemplate.postForEntity("/time-category", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `add timeCategory without name returns 400`() {
        val httpEntity = jsonPostEntity("timeCategory/invalidCreation.json")
        val response = restTemplate.postForEntity("/time-category", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `update timeCategory returns 200`() {
        val httpEntity = jsonPostEntity("timeCategory/validUpdate.json")
        val response = restTemplate.exchange(
            URI("/time-category"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `update timeCategory without name returns 400`() {
        val httpEntity = jsonPostEntity("timeCategory/invalidUpdate.json")
        val response = restTemplate.exchange(
            URI("/time-category"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `delete timeCategory returns 204`() {
        val response = restTemplate.exchange(
            URI("/time-category/1"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete without path id returns 400`() {
        val response = restTemplate.exchange(
            URI("/time-category"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @Test
    fun `get all timeCategories returns 200`() {
        val response = restTemplate.getForEntity("/time-category", ArrayNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}
