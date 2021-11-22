package three.consulting.epoc.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.EmployeeDTO

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

    @Test
    fun `added employee is found from the database`() {
        val employee = EmployeeDTO(
            first_name = "Esimerkki",
            last_name = "Testaaja",
            email = "esimerkki@testaaja.fi"
        )
        val addedEmployee: EmployeeDTO = employeeService.createEmployee(employee)
        assertThat(addedEmployee.first_name).isEqualTo(employee.first_name)
        assertThat(addedEmployee.last_name).isEqualTo(employee.last_name)
        assertThat(addedEmployee.email).isEqualTo(employee.email)
    }

    @Test
    fun `adding employee with id fails`() {
        val invalidEmployee = EmployeeDTO(
            id = 2,
            first_name = "Esimerkki",
            last_name = "Testaja",
            email = "esimerkki@testaaja.fi"
        )
        assertThatThrownBy { employeeService.createEmployee(invalidEmployee) }
            .isInstanceOf(UnableToCreateEmployeeException::class.java)
            .hasMessage("Cannot create an employee with existing id")
    }

    @Test
    fun `update employee with id changes updated time`() {
        val existingEmployee = employeeService.findEmployeeForId(1L)
        if (existingEmployee != null) {
            val updatedEmployee = employeeService.updateEmployeeForId(existingEmployee)
            assertThat(updatedEmployee.updated).isNotEqualTo(existingEmployee.updated)
        }
    }

    @Test
    fun `update employee without id raises error`() {
        val invalidEmployee = EmployeeDTO(
            first_name = "Test",
            last_name = "Failure",
            email = "test@failure.fi"
        )
        assertThatThrownBy { employeeService.updateEmployeeForId(invalidEmployee) }
            .isInstanceOf(UnableToUpdateEmployeeException::class.java)
            .hasMessage("Cannot update employee, missing employee id")
    }

    @Test
    fun `delete employee removes employee from database`() {
        assertThat(employeeService.findEmployeeForId(2L)).isNotNull
        employeeService.deleteEmployee(2L)
        assertThat(employeeService.findEmployeeForId(2L)).isNull()
    }

    @Test
    fun `delete employee with non-existing id raise error`() {
        assertThatThrownBy { employeeService.deleteEmployee(1000L) }
            .isInstanceOf(UnableToDeleteEmployeeException::class.java)
            .hasMessage("Cannot delete employee, no employee found for given id: 1000")
    }

    @Test
    fun `get all employees`() {
        val employees = employeeService.findAllEmployees()
        assertThat(employees.map { it.first_name }).containsExactlyElementsOf(listOf("Testi", "Test"))
    }
}
