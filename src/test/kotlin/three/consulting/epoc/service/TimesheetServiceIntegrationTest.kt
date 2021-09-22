package three.consulting.epoc.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.dto.TimesheetDTO
import java.time.LocalDate

@ContextConfiguration(classes = [TimesheetService::class])
class TimesheetServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var timesheetService: TimesheetService

    @Test
    fun `searching a timesheet for id return a timesheet`() {
        val timesheet: TimesheetDTO = timesheetService.findTimesheetForId(1L)!!
        assertThat(timesheet.name).isEqualTo("test")
        assertThat(timesheet.description).isEqualTo("testing")
        assertThat(timesheet.project.id).isEqualTo(1L)
    }
    @Test
    fun `searching a timesheet for invalid id return null`() {
        val timesheet: TimesheetDTO? = timesheetService.findTimesheetForId(1000L)
        assertThat(timesheet).isNull()
    }

    @Test
    fun `added timesheet is found from the database`() {
        val timesheet = TimesheetDTO(
            name = "Sample",
            description = "Sample timesheet",
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
            ),
            employee = EmployeeDTO(2, "New", "Timesheet-worker", "new.timesheet@worker.fi"),
        )
        val addedTimesheet: TimesheetDTO = timesheetService.createTimesheet(timesheet)
        assertThat(addedTimesheet.name).isEqualTo(timesheet.name)
        assertThat(addedTimesheet.description).isEqualTo(timesheet.description)
    }

    @Test
    fun `adding timesheet with id fails`() {
        val invalidTimesheet = TimesheetDTO(
            id = 2,
            name = "asd",
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
            ),
            employee = EmployeeDTO(1, "Failing", "Timesheet-Worker", "failing-worker@timesheet.fi"),
        )
        Assertions.assertThatThrownBy { timesheetService.createTimesheet(invalidTimesheet) }
            .isInstanceOf(UnableToCreateTimesheetException::class.java)
            .hasMessage("Cannot create a timesheet with existing id")
    }

    @Test
    fun `adding timesheet with non-existing relation fails`() {
        val invalidTimesheet = TimesheetDTO(
            name = "asd",
            allocation = 100,
            project = ProjectDTO(
                id = 100L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
            ),
            employee = EmployeeDTO(100L, "Failing", "Timesheet-Worker", "failing-worker@timesheet.fi"),
        )
        Assertions.assertThatThrownBy { timesheetService.createTimesheet(invalidTimesheet) }
            .isInstanceOf(UnableToCreateTimesheetException::class.java)
            .hasMessage("Cannot create a timesheet with non-existing relation")
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
    fun `update timesheet without id raises error`() {
        val invalidTimesheet = TimesheetDTO(
            name = "asd",
            allocation = 100,
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
            ),
            employee = EmployeeDTO(1, "Failing", "Timesheet-Worker", "failing-worker@timesheet.fi"),
        )
        Assertions.assertThatThrownBy { timesheetService.updateTimesheetForId(invalidTimesheet) }
            .isInstanceOf(UnableToUpdateTimesheetException::class.java)
            .hasMessage("Cannot update timesheet, missing timesheet id")
    }

    @Test
    fun `delete timesheet removes timesheet from database`() {
        assertThat(timesheetService.findTimesheetForId(2L)).isNotNull
        timesheetService.deleteTimesheet(2L)
        assertThat(timesheetService.findTimesheetForId(2L)).isNull()
    }

    @Test
    fun `delete timesheet with non-existing id raise error`() {
        Assertions.assertThatThrownBy { timesheetService.deleteTimesheet(1000L) }
            .isInstanceOf(UnableToDeleteTimesheetException::class.java)
            .hasMessage("Cannot delete timesheet, no timesheet found for given id: 1000")
    }
}
