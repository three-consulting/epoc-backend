package three.consulting.epoc.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class SwaggerIntegrationTest : ControllerIntegrationTest() {

    @Test
    fun `docs returns 200`() {
        val response = restTemplate.getForEntity("/docs", ObjectNode::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `docs-ui returns 302 found`() {
        val response = restTemplate.getForEntity("/docs-ui.html", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FOUND)
    }
}
