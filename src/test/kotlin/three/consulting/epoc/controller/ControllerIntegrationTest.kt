package three.consulting.epoc.controller

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import three.consulting.epoc.service.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@EnableAutoConfiguration(exclude = [ SecurityAutoConfiguration::class ])
@AutoConfigureTestDatabase
@AutoConfigureEmbeddedDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @MockBean
    lateinit var customerService: CustomerService

    @MockBean
    lateinit var employeeService: EmployeeService

    @MockBean
    lateinit var projectService: ProjectService

    @MockBean
    lateinit var timesheetService: TimesheetService

    @MockBean
    lateinit var timeCategoryService: TimeCategoryService

    @MockBean
    lateinit var taskService: TaskService

    @MockBean
    lateinit var timesheetEntryService: TimesheetEntryService
}
