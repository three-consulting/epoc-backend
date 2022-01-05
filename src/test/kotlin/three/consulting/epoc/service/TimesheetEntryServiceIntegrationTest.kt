package three.consulting.epoc.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.*
import three.consulting.epoc.entity.Status
import java.time.LocalDate
import java.time.Duration as Duration

@ContextConfiguration(classes = [TimesheetEntryService::class])
class TimesheetEntryServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var timesheetEntryService: TimesheetEntryService

    @Test
    fun `searching a timesheetEntry for id return a timesheetEntry`() {
        val timesheetEntry: TimesheetEntryDTO = timesheetEntryService.findTimesheetEntryForId(1L)!!
        assertThat(timesheetEntry.description).isEqualTo("Testing timesheet entry")
        assertThat(timesheetEntry.task.id).isEqualTo(1L)
    }
    @Test
    fun `searching a timesheetEntry for invalid id return null`() {
        val timesheetEntry: TimesheetEntryDTO? = timesheetEntryService.findTimesheetEntryForId(1000L)
        assertThat(timesheetEntry).isNull()
    }

    @Test
    fun `added timesheetEntry is found from the database`() {
        val timesheetEntry = TimesheetEntryDTO(
            description = "Sample timesheetEntry",
            quantity = Duration.ofSeconds(100L),
            date = LocalDate.now(),
            timesheet = TimesheetDTO(
                id = 1L,
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
                status = Status.ACTIVE,
            ),
            timeCategory = TimeCategoryDTO(id = 1L, name = "Test Category"),
            task = TaskDTO(
                id = 1L,
                name = "Sample",
                description = "Sample task",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                project = ProjectDTO(
                    id = 1L,
                    name = "Sample",
                    description = "Sample project",
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    customer = CustomerDTO(1, "New Project Customer"),
                    managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
                ),
            )
        )
        val addedTimesheetEntry: TimesheetEntryDTO = timesheetEntryService.createTimesheetEntry(timesheetEntry)
        assertThat(addedTimesheetEntry.description).isEqualTo(timesheetEntry.description)
    }

    @Test
    fun `adding timesheetEntry with id fails`() {
        val invalidTimesheetEntry = TimesheetEntryDTO(
            id = 2,
            description = "Sample timesheetEntry",
            quantity = Duration.ofSeconds(100),
            date = LocalDate.now(),
            timesheet = TimesheetDTO(
                id = 1L,
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
                status = Status.ACTIVE,
            ),
            timeCategory = TimeCategoryDTO(id = 1L, name = "Test Category"),
            task = TaskDTO(
                id = 1L,
                name = "Sample",
                description = "Sample task",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                project = ProjectDTO(
                    id = 1L,
                    name = "Sample",
                    description = "Sample project",
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    customer = CustomerDTO(1, "New Project Customer"),
                    managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
                ),
            )
        )
        Assertions.assertThatThrownBy { timesheetEntryService.createTimesheetEntry(invalidTimesheetEntry) }
            .isInstanceOf(UnableToCreateTimesheetEntryException::class.java)
            .hasMessage("Cannot create a timesheetEntry with existing id")
    }

    @Test
    fun `adding timesheetEntry with non-existing relation fails`() {
        val invalidTimesheetEntry = TimesheetEntryDTO(
            description = "Sample timesheetEntry",
            quantity = Duration.ofSeconds(100),
            date = LocalDate.now(),
            timesheet = TimesheetDTO(
                id = 10L,
                name = "Sample",
                description = "Sample timesheet",
                allocation = 100,
                project = ProjectDTO(
                    id = 1L,
                    name = "Sample",
                    description = "Sample project",
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    customer = CustomerDTO(10, "New Project Customer"),
                    managingEmployee = EmployeeDTO(10, "New", "Project-worker", "new.project@worker.fi"),
                ),
                employee = EmployeeDTO(2, "New", "Timesheet-worker", "new.timesheet@worker.fi"),
                status = Status.ACTIVE,
            ),
            timeCategory = TimeCategoryDTO(id = 1L, name = "Test Category"),
            task = TaskDTO(
                id = 1L,
                name = "Sample",
                description = "Sample task",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                project = ProjectDTO(
                    id = 1L,
                    name = "Sample",
                    description = "Sample project",
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    customer = CustomerDTO(1, "New Project Customer"),
                    managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
                ),
            )
        )
        Assertions.assertThatThrownBy { timesheetEntryService.createTimesheetEntry(invalidTimesheetEntry) }
            .isInstanceOf(UnableToCreateTimesheetEntryException::class.java)
            .hasMessage("Cannot create a timesheetEntry with non-existing relation")
    }

    @Test
    fun `update timesheetEntry with id changes updated time`() {
        val existingTimesheetEntry = timesheetEntryService.findTimesheetEntryForId(1L)
        if (existingTimesheetEntry != null) {
            val updatedTimesheetEntry = timesheetEntryService.updateTimesheetEntryForId(existingTimesheetEntry)
            assertThat(updatedTimesheetEntry.updated).isNotEqualTo(existingTimesheetEntry.updated)
        }
    }

    @Test
    fun `update timesheetEntry without id raises error`() {
        val invalidTimesheetEntry = TimesheetEntryDTO(
            description = "Sample timesheetEntry",
            quantity = Duration.ofSeconds(100),
            date = LocalDate.now(),
            timesheet = TimesheetDTO(
                id = 1L,
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
                status = Status.ACTIVE,
            ),
            timeCategory = TimeCategoryDTO(id = 1L, name = "Test Category"),
            task = TaskDTO(
                id = 1L,
                name = "Sample",
                description = "Sample task",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                project = ProjectDTO(
                    id = 1L,
                    name = "Sample",
                    description = "Sample project",
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    customer = CustomerDTO(1, "New Project Customer"),
                    managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
                ),
            )
        )
        Assertions.assertThatThrownBy { timesheetEntryService.updateTimesheetEntryForId(invalidTimesheetEntry) }
            .isInstanceOf(UnableToUpdateTimesheetEntryException::class.java)
            .hasMessage("Cannot update timesheetEntry, missing timesheetEntry id")
    }

    @Test
    fun `delete timesheetEntry removes timesheetEntry from database`() {
        assertThat(timesheetEntryService.findTimesheetEntryForId(2L)).isNotNull
        timesheetEntryService.deleteTimesheetEntry(2L)
        assertThat(timesheetEntryService.findTimesheetEntryForId(2L)).isNull()
    }

    @Test
    fun `delete timesheetEntry with non-existing id raise error`() {
        Assertions.assertThatThrownBy { timesheetEntryService.deleteTimesheetEntry(1000L) }
            .isInstanceOf(UnableToDeleteTimesheetEntryException::class.java)
            .hasMessage("Cannot delete timesheetEntry, no timesheetEntry found for given id: 1000")
    }
}
