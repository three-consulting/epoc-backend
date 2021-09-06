package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class CustomerControllerIntegrationTest : ControllerIntegrationTest() {

    @Test
    fun `get customer for id returns 200`() {
        val response = restTemplate.getForEntity("/customer/1", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}
