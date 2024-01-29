package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ActuatorIntegrationTest : ControllerIntegrationTest() {
    @Test
    fun `actuator health returns 200`() {
        val response = restTemplate.getForEntity("/actuator/health", ObjectNode::class.java)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}
