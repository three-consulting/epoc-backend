package three.consulting.epoc.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.common.Role
import three.consulting.epoc.common.Status
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.repository.EmployeeRepository

@ContextConfiguration(classes = [EmployeeService::class])
class EmployeeServiceIntegrationTest : IntegrationTest() {
    @Autowired
    private lateinit var employeeService: EmployeeService

    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @Test
    fun `searching an employee for id return an employee`() {
        val employee: EmployeeDTO = employeeService.findEmployeeForId(1L)!!
        assertThat(employee.firstName).isEqualTo("Testi")
        assertThat(employee.lastName).isEqualTo("Tekijä")
    }

    @Test
    fun `searching a employee for an invalid id throws an exception`() {
        assertThatThrownBy { employeeService.findEmployeeForId(1000000L) }
            .isInstanceOf(EmployeeNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Employee not found for id: 1000000\"")
    }

    @Test
    fun `added employee is found from the database`() {
        val employee =
            EmployeeDTO(
                firstName = "Esimerkki",
                lastName = "Testaaja",
                email = "esimerkki@testaaja.fi",
                role = Role.USER
            )
        val addedEmployee: EmployeeDTO = employeeService.createEmployee(employee)
        assertThat(addedEmployee.firstName).isEqualTo(employee.firstName)
        assertThat(addedEmployee.lastName).isEqualTo(employee.lastName)
        assertThat(addedEmployee.email).isEqualTo(employee.email)
    }

    @Test
    fun `adding employee with id fails`() {
        val invalidEmployee =
            EmployeeDTO(
                id = 2,
                firstName = "Esimerkki",
                lastName = "Testaja",
                email = "esimerkki@testaaja.fi",
                role = Role.USER
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
        val invalidEmployee =
            EmployeeDTO(
                firstName = "Test",
                lastName = "Failure",
                email = "test@failure.fi",
                role = Role.USER
            )
        assertThatThrownBy { employeeService.updateEmployeeForId(invalidEmployee) }
            .isInstanceOf(UnableToUpdateEmployeeException::class.java)
            .hasMessage("Cannot update employee, missing employee id")
    }

    @Test
    fun `update archived employee raises error`() {
        val archivedEmployee =
            EmployeeDTO(
                firstName = "Test",
                lastName = "Archived",
                email = "test@archived.com",
                role = Role.USER,
                status = Status.ARCHIVED
            )
        val id = employeeService.createEmployee(archivedEmployee).id
        if (id != null) {
            val updatedEmployee =
                EmployeeDTO(
                    id = id,
                    firstName = "Testest",
                    lastName = "Archived",
                    email = "testest@archived.com",
                    role = Role.ADMIN,
                    status = Status.ARCHIVED
                )
            assertThatThrownBy { employeeService.updateEmployeeForId(updatedEmployee) }
                .isInstanceOf(UnableToUpdateEmployeeException::class.java)
                .hasMessage("Cannot update archived employee")
        }
    }

    @Test
    fun `unarchive employee`() {
        val archivedEmployee =
            EmployeeDTO(
                firstName = "Test",
                lastName = "Archived",
                email = "test@archived.com",
                role = Role.USER,
                status = Status.ARCHIVED
            )
        val id = employeeService.createEmployee(archivedEmployee).id
        if (id != null) {
            val unarchivedEmployee =
                EmployeeDTO(
                    id = id,
                    firstName = "Test",
                    lastName = "Archived",
                    email = "test@archived.com",
                    role = Role.USER,
                    status = Status.ACTIVE
                )
            val updatedEmployee = employeeService.updateEmployeeForId(unarchivedEmployee)
            assertThat(updatedEmployee.id).isEqualTo(id)
            assertThat(updatedEmployee.status).isEqualTo(Status.ACTIVE)
        }
    }

    @Test
    fun `delete employee removes employee from database`() {
        assertThat(employeeRepository.findByIdOrNull(4L)).isNotNull
        employeeService.deleteEmployee(4L)
        assertThat(employeeRepository.findByIdOrNull(4L)).isNull()
    }

    @Test
    fun `get all employees`() {
        val employees = employeeService.findAllEmployees()
        assertThat(employees.map { it.firstName }).containsExactlyElementsOf(listOf("Testi", "Test", "Matti", "Teesti"))
    }
}
