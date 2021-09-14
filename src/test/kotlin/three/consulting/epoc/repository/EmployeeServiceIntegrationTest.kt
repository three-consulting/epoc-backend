package three.consulting.epoc.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.service.EmployeeService

@ContextConfiguration(classes = [EmployeeService::class])
class EmployeeServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var employeeService: EmployeeService

    @Test
    fun `searching an employee for id return an employee`() {
        val employee: EmployeeDTO = employeeService.findEmployeeForId(1L)!!
        assertThat(employee.first_name).isEqualTo("Testi")
        assertThat(employee.last_name).isEqualTo("Tekijä")
    }

    @Test
    fun `searching a employee for an invalid id return null`() {
        val employee: EmployeeDTO? = employeeService.findEmployeeForId(1000L)
        assertThat(employee).isNull()
    }
}
