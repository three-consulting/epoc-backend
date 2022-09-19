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
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.dto.TimesheetDTO
import three.consulting.epoc.repository.TimesheetRepository
import java.time.LocalDate

@ContextConfiguration(classes = [TimesheetService::class])
class TimesheetServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var timesheetService: TimesheetService

    @Autowired
    private lateinit var timesheetRepository: TimesheetRepository

    val newProjectWorkerId1 = EmployeeDTO(
        id = 1,
        firstName = "New",
        lastName = "Project-worker",
        email = "new.project@worker.fi",
        role = Role.USER
    )

    val mattiWorkerId4 = EmployeeDTO(
        id = 4,
        firstName = "Matti",
        lastName = "Meikälainen",
        email = "matti@worker.fi",
        role = Role.USER
    )

    val testWorkerId2 = EmployeeDTO(
        id = 2,
        firstName = "Test",
        lastName = "Worker",
        email = "test@worker.fi",
        role = Role.USER
    )

    val failingTimesheetWorkerId1 = EmployeeDTO(
        id = 1,
        firstName = "Failing",
        lastName = "Timesheet-Worker",
        email = "failing-worker@timesheet.fi",
        role = Role.USER
    )

    val failingTimesheetWorkerId100L = EmployeeDTO(
        id = 100L,
        firstName = "Failing",
        lastName = "Timesheet-Worker",
        email = "failing-worker@timesheet.fi",
        role = Role.USER
    )

    @Test
    fun `searching a timesheet for id return a timesheet`() {
        val timesheet: TimesheetDTO = timesheetService.findTimesheetForId(1L)!!
        assertThat(timesheet.name).isEqualTo("test")
        assertThat(timesheet.description).isEqualTo("testing")
        assertThat(timesheet.project.id).isEqualTo(1L)
    }

    @Test
    fun `searching a timesheet for invalid id return null`() {
        assertThatThrownBy { timesheetService.findTimesheetForId(1000000L) }
            .isInstanceOf(TimesheetNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Timesheet not found for id: 1000000\"")
    }

    @Test
    fun `added timesheet is found from the database`() {
        val timesheet = TimesheetDTO(
            name = "Sample",
            description = "Sample timesheet",
            rate = 100.0f,
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
            employee = mattiWorkerId4,
        )
        val addedTimesheet: TimesheetDTO = timesheetService.createTimesheet(timesheet)
        assertThat(addedTimesheet.name).isEqualTo(timesheet.name)
        assertThat(addedTimesheet.description).isEqualTo(timesheet.description)
    }

    @Test
    fun `adding timesheet with non-unique project and employee fails`() {
        val invalidTimesheet = TimesheetDTO(
            name = "Sample",
            description = "Sample timesheet",
            rate = 100.0f,
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
            employee = testWorkerId2,
        )
        assertThatThrownBy { timesheetService.createTimesheet(invalidTimesheet) }
            .isInstanceOf(UnableToCreateTimesheetException::class.java)
            .hasMessage("Cannot create a timesheet that violates data integrity")
    }

    @Test
    fun `adding timesheet with id fails`() {
        val invalidTimesheet = TimesheetDTO(
            id = 2,
            name = "asd",
            rate = 100.0f,
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
            employee = failingTimesheetWorkerId1,
        )
        assertThatThrownBy { timesheetService.createTimesheet(invalidTimesheet) }
            .isInstanceOf(UnableToCreateTimesheetException::class.java)
            .hasMessage("Cannot create a timesheet with existing id")
    }

    @Test
    fun `adding timesheet with non-existing relation fails`() {
        val invalidTimesheet = TimesheetDTO(
            name = "asd",
            allocation = 100,
            rate = 100.0f,
            project = ProjectDTO(
                id = 100L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
            employee = failingTimesheetWorkerId100L,
        )
        assertThatThrownBy { timesheetService.createTimesheet(invalidTimesheet) }
            .isInstanceOf(UnableToCreateTimesheetException::class.java)
            .hasMessage("Cannot create a timesheet that violates data integrity")
    }

    @Test
    fun `update timesheet with id changes updated time`() {
        val existingTimesheet = timesheetService.findTimesheetForId(1L)
        if (existingTimesheet != null) {
            val updatedTimesheet = timesheetService.updateTimesheetForId(existingTimesheet)
            assertThat(updatedTimesheet.updated).isNotEqualTo(existingTimesheet.updated)
        }
    }

    @Test
    fun `update timesheet with status archived changes status value`() {
        val timesheet = TimesheetDTO(
            name = "Sample",
            description = "Sample timesheet",
            rate = 100.0f,
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
            employee = mattiWorkerId4,
            status = Status.ACTIVE
        )
        val addedTimesheet: TimesheetDTO = timesheetService.createTimesheet(timesheet)
        assertThat(addedTimesheet.name).isEqualTo(timesheet.name)
        assertThat(addedTimesheet.description).isEqualTo(timesheet.description)

        val archivedTimesheet = TimesheetDTO(
            id = addedTimesheet.id,
            name = "Sample",
            description = "Sample timesheet",
            rate = 100.0f,
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
            employee = mattiWorkerId4,
            status = Status.ARCHIVED
        )
        val updatedTimesheet = timesheetService.updateTimesheetForId(archivedTimesheet)
        assertThat(archivedTimesheet.status).isEqualTo(Status.ARCHIVED)
        assertThat(updatedTimesheet.updated).isNotEqualTo(addedTimesheet.updated)
        assertThat(updatedTimesheet.status).isNotEqualTo(addedTimesheet.status)
        assertThat(updatedTimesheet.status).isEqualTo(Status.ARCHIVED)
    }

    @Test
    fun `update timesheet without id raises error`() {
        val invalidTimesheet = TimesheetDTO(
            name = "asd",
            rate = 100.0f,
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
            employee = failingTimesheetWorkerId1,
        )
        assertThatThrownBy { timesheetService.updateTimesheetForId(invalidTimesheet) }
            .isInstanceOf(UnableToUpdateTimesheetException::class.java)
            .hasMessage("Cannot update timesheet, missing timesheet id")
    }

    @Test
    fun `delete timesheet removes timesheet from database`() {
        assertThat(timesheetRepository.findByIdOrNull(3L)).isNotNull
        timesheetService.deleteTimesheet(3L)
        assertThat(timesheetRepository.findByIdOrNull(3L)).isNull()
    }

    @Test
    fun `delete timesheet with non-existing id raise error`() {
        assertThatThrownBy { timesheetService.deleteTimesheet(1000L) }
            .isInstanceOf(UnableToDeleteTimesheetException::class.java)
            .hasMessage("Cannot delete timesheet, no timesheet found for given id: 1000")
    }

    @Test
    fun `searching timesheet with project id 1 returns array of timesheet objects`() {
        val timesheets = timesheetService.findTimesheets(1L, null, null)
        assertThat(timesheets).hasSize(3)
    }

    @Test
    fun `searching timesheet with project id 99 returns empty array`() {
        val timesheets = timesheetService.findTimesheets(99L, null, null)
        assertThat(timesheets).hasSize(0)
    }

    @Test
    fun `searching timesheets with employeeId 2 returns a timesheet`() {
        val timesheets = timesheetService.findTimesheets(null, 2L, null)
        assertThat(timesheets).hasSize(1)
        assertThat(timesheets.first().name).isEqualTo("test2")
    }

    @Test
    fun `searching timesheets with employeeId and projectId returns a timesheet`() {
        val timesheets = timesheetService.findTimesheets(1L, 1L, null)
        assertThat(timesheets).hasSize(1)
        assertThat(timesheets.first().name).isEqualTo("test")
    }

    @Test
    fun `searching timesheets with email returns timesheets for that employee`() {
        val timesheets = timesheetService.findTimesheets(null, null, "testi@tekija.fi")
        assertThat(timesheets).hasSize(1)
        assertThat(timesheets.first().employee.id).isEqualTo(1)
    }

    @Test
    fun `searching timesheets without request parameters returns all timesheets`() {
        val timesheets = timesheetService.findTimesheets(null, null, null)
        assertThat(timesheets).hasSize(3)
        assertThat(timesheets.first().employee.id).isEqualTo(1)
    }
}
