package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import three.consulting.epoc.utils.jsonPostEntity
import java.net.URI

class CustomerControllerIntegrationTest : ControllerIntegrationTest() {

    @Test
    fun `get customer for id returns 200`() {
        val response = restTemplate.getForEntity("/customer/1", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `add customer returns 200`() {
        val httpEntity = jsonPostEntity("src/test/resources/customer/validCreation.json"
        )
        val response = restTemplate.postForEntity("/customer", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `add customer without name returns 400`() {
        val httpEntity = jsonPostEntity("src/test/resources/customer/invalidCreation.json"
        )
        val response = restTemplate.postForEntity("/customer", httpEntity, ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `update customer return 200`() {
        val httpEntity = jsonPostEntity("src/test/resources/customer/validUpdate.json")
        val response = restTemplate.exchange(
            URI("/customer"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `update customer without name return 400`() {
        val httpEntity = jsonPostEntity("src/test/resources/customer/invalidUpdate.json")
        val response = restTemplate.exchange(
            URI("/customer"),
            HttpMethod.PUT,
            httpEntity,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `delete customer return 204`() {
        val response = restTemplate.exchange(
            URI("/customer/1"),
            HttpMethod.DELETE,
            null,
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete without path id return 400`() {
        val response = restTemplate.exchange(
            URI("/customer"),
            HttpMethod.DELETE,
            jsonPostEntity("src/test/resources/customer/validCreation.json"),
            ObjectNode::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }
}
