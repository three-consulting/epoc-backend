package three.consulting.epoc.controller

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import three.consulting.epoc.config.FirebaseConfig
import three.consulting.epoc.service.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("cit")
@EnableAutoConfiguration(exclude = [ SecurityAutoConfiguration::class, DataSourceAutoConfiguration::class ])
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
    lateinit var taskService: TaskService

    @MockBean
    lateinit var timesheetEntryService: TimesheetEntryService

    @MockBean
    lateinit var firebaseService: FirebaseService

    @MockBean
    lateinit var firebaseConfig: FirebaseConfig
}
