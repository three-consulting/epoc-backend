package three.consulting.epoc.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.common.Role
import three.consulting.epoc.common.Status
import three.consulting.epoc.dto.*
import three.consulting.epoc.repository.TimesheetEntryRepository
import java.time.LocalDate

@ContextConfiguration(classes = [TimesheetEntryService::class])
@Transactional
class TimesheetEntryServiceIntegrationTest : IntegrationTest() {
    @Autowired
    private lateinit var timesheetEntryService: TimesheetEntryService

    @Autowired
    private lateinit var timesheetEntryRepository: TimesheetEntryRepository

    val newProjectWorkerId1 = EmployeeDTO(id = 1, firstName = "New", lastName = "Project-worker", email = "new.project@worker.fi", role = Role.USER)
    val newTimesheetWorkerId2 = EmployeeDTO(id = 2, firstName = "New", lastName = "Timesheet-worker", email = "new.timesheet@worker.fi", role = Role.USER)
    val newProjectWorkerId10 = EmployeeDTO(id = 10, firstName = "New", lastName = "Project-worker", email = "new.project@worker.fi", role = Role.USER)

    val sampleTimesheetEntry =
        TimesheetEntryDTO(
            description = "Sample timesheetEntry",
            quantity = 7.5f,
            date = LocalDate.now(),
            timesheet =
                TimesheetDTO(
                    id = 1L,
                    name = "Sample",
                    description = "Sample timesheet",
                    rate = 100.0f,
                    allocation = 100,
                    project =
                        ProjectDTO(
                            id = 1L,
                            name = "Sample",
                            description = "Sample project",
                            startDate = LocalDate.now(),
                            endDate = LocalDate.now(),
                            customer = CustomerDTO(1, "New Project Customer"),
                            managingEmployee = newProjectWorkerId1,
                        ),
                    employee = newTimesheetWorkerId2,
                    status = Status.ACTIVE,
                ),
            task =
                TaskDTO(
                    id = 1L,
                    name = "Sample",
                    description = "Sample task",
                    project =
                        ProjectDTO(
                            id = 1L,
                            name = "Sample",
                            description = "Sample project",
                            startDate = LocalDate.now(),
                            endDate = LocalDate.now(),
                            customer = CustomerDTO(1, "New Project Customer"),
                            managingEmployee = newProjectWorkerId1,
                        ),
                ),
            flex = 0f
        )

    val sampleTimesheetEntries = listOf(sampleTimesheetEntry, sampleTimesheetEntry)

    @Test
    fun `searching a timesheetEntry for id return a timesheetEntry`() {
        val timesheetEntry: TimesheetEntryDTO = timesheetEntryService.findTimesheetEntryForId(1L)!!
        assertThat(timesheetEntry.description).isEqualTo("Testing timesheet entry")
        assertThat(timesheetEntry.task.id).isEqualTo(1L)
        assertThat(timesheetEntry.flex).isEqualTo(1.5f)
    }

    @Test
    fun `searching a timesheetEntry for invalid id return null`() {
        assertThatThrownBy { timesheetEntryService.findTimesheetEntryForId(1000000L) }
            .isInstanceOf(TimeSheetEntryNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Timesheet entry not found for id: 1000000\"")
    }

    @Test
    fun `searching an employee flex for email`() {
        val addedTimesheetEntry: TimesheetEntryDTO = timesheetEntryService.createTimesheetEntry(sampleTimesheetEntry)
        assertThat(addedTimesheetEntry.flex).isEqualTo(0f)
    }

    @Test
    fun `searching an employee flex for invalid email`() {
        assertThatThrownBy { timesheetEntryService.findEmployeeFlexByEmail("koira@tekija.fi") }
            .isInstanceOf(FlexNotFoundException::class.java)
    }

    @Test
    fun `added timesheetEntry is found from the database`() {
        val addedTimesheetEntry: TimesheetEntryDTO = timesheetEntryService.createTimesheetEntry(sampleTimesheetEntry)
        assertThat(addedTimesheetEntry.description).isEqualTo(sampleTimesheetEntry.description)
    }

    @Test
    fun `added timesheetEntries are found from the database`() {
        val addedTimesheetEntries = timesheetEntryService.createTimesheetEntries(sampleTimesheetEntries)
        assertThat(addedTimesheetEntries.size == sampleTimesheetEntries.size)
        addedTimesheetEntries.forEach { assertThat(it.description).isEqualTo(sampleTimesheetEntry.description) }
    }

    @Test
    fun `authorized employee is allowed to create timesheet entries`() {
        val isAllowed = timesheetEntryService.hasValidEmails(sampleTimesheetEntries, newTimesheetWorkerId2.email)
        assertThat(isAllowed).isTrue
    }

    @Test
    fun `not authorized employee is not allowed to create timesheet entries`() {
        val isAllowed = timesheetEntryService.hasValidEmails(sampleTimesheetEntries, newProjectWorkerId1.email)
        assertThat(isAllowed).isFalse
    }

    @Test
    fun `adding timesheetEntry with id fails`() {
        val invalidTimesheetEntry =
            TimesheetEntryDTO(
                id = 2,
                description = "Sample timesheetEntry",
                quantity = 7.5f,
                date = LocalDate.now(),
                timesheet =
                    TimesheetDTO(
                        id = 1L,
                        name = "Sample",
                        description = "Sample timesheet",
                        rate = 100.0f,
                        allocation = 100,
                        project =
                            ProjectDTO(
                                id = 1L,
                                name = "Sample",
                                description = "Sample project",
                                startDate = LocalDate.now(),
                                endDate = LocalDate.now(),
                                customer = CustomerDTO(1, "New Project Customer"),
                                managingEmployee = newProjectWorkerId1,
                            ),
                        employee = newTimesheetWorkerId2,
                        status = Status.ACTIVE,
                    ),
                task =
                    TaskDTO(
                        id = 1L,
                        name = "Sample",
                        description = "Sample task",
                        project =
                            ProjectDTO(
                                id = 1L,
                                name = "Sample",
                                description = "Sample project",
                                startDate = LocalDate.now(),
                                endDate = LocalDate.now(),
                                customer = CustomerDTO(1, "New Project Customer"),
                                managingEmployee = newProjectWorkerId1,
                            ),
                    )
            )
        assertThatThrownBy { timesheetEntryService.createTimesheetEntry(invalidTimesheetEntry) }
            .isInstanceOf(UnableToCreateTimesheetEntryException::class.java)
            .hasMessage("Cannot create a timesheetEntry with existing id")
    }

    @Test
    fun `adding timesheetEntry with non-existing relation fails`() {
        val invalidTimesheetEntry =
            TimesheetEntryDTO(
                description = "Sample timesheetEntry",
                quantity = 7.5f,
                date = LocalDate.now(),
                timesheet =
                    TimesheetDTO(
                        id = 10L,
                        name = "Sample",
                        description = "Sample timesheet",
                        rate = 100.0f,
                        allocation = 100,
                        project =
                            ProjectDTO(
                                id = 1L,
                                name = "Sample",
                                description = "Sample project",
                                startDate = LocalDate.now(),
                                endDate = LocalDate.now(),
                                customer = CustomerDTO(10, "New Project Customer"),
                                managingEmployee = newProjectWorkerId10,
                            ),
                        employee = newTimesheetWorkerId2,
                        status = Status.ACTIVE,
                    ),
                task =
                    TaskDTO(
                        id = 1L,
                        name = "Sample",
                        description = "Sample task",
                        project =
                            ProjectDTO(
                                id = 1L,
                                name = "Sample",
                                description = "Sample project",
                                startDate = LocalDate.now(),
                                endDate = LocalDate.now(),
                                customer = CustomerDTO(1, "New Project Customer"),
                                managingEmployee = newProjectWorkerId1,
                            ),
                    )
            )
        assertThatThrownBy { timesheetEntryService.createTimesheetEntry(invalidTimesheetEntry) }
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
        val invalidTimesheetEntry =
            TimesheetEntryDTO(
                description = "Sample timesheetEntry",
                quantity = 7.5f,
                date = LocalDate.now(),
                timesheet =
                    TimesheetDTO(
                        id = 1L,
                        name = "Sample",
                        description = "Sample timesheet",
                        rate = 100f,
                        allocation = 100,
                        project =
                            ProjectDTO(
                                id = 1L,
                                name = "Sample",
                                description = "Sample project",
                                startDate = LocalDate.now(),
                                endDate = LocalDate.now(),
                                customer = CustomerDTO(1, "New Project Customer"),
                                managingEmployee = newProjectWorkerId1,
                            ),
                        employee = newTimesheetWorkerId2,
                        status = Status.ACTIVE,
                    ),
                task =
                    TaskDTO(
                        id = 1L,
                        name = "Sample",
                        description = "Sample task",
                        project =
                            ProjectDTO(
                                id = 1L,
                                name = "Sample",
                                description = "Sample project",
                                startDate = LocalDate.now(),
                                endDate = LocalDate.now(),
                                customer = CustomerDTO(1, "New Project Customer"),
                                managingEmployee = newProjectWorkerId1,
                            ),
                    )
            )
        assertThatThrownBy { timesheetEntryService.updateTimesheetEntryForId(invalidTimesheetEntry) }
            .isInstanceOf(UnableToUpdateTimesheetEntryException::class.java)
            .hasMessage("Cannot update timesheetEntry, missing timesheetEntry id")
    }

    @Test
    fun `update timesheetEntries changes the updated time`() {
        val firstTimesheetEntry = timesheetEntryService.findTimesheetEntryForId(1L)
        val secondTimesheetEntry = timesheetEntryService.findTimesheetEntryForId(2L)
        if (firstTimesheetEntry != null && secondTimesheetEntry != null) {
            val updatedTimesheetEntries = timesheetEntryService.updateTimesheetEntriesForId(listOf(firstTimesheetEntry, secondTimesheetEntry))
            val updated = updatedTimesheetEntries.map { it.updated }
            assertThat(updated[0]).isNotEqualTo(firstTimesheetEntry.updated)
            assertThat(updated[1]).isNotEqualTo(secondTimesheetEntry.updated)
        }
    }

    @Test
    fun `delete timesheetEntry removes timesheetEntry from database`() {
        assertThat(timesheetEntryRepository.findByIdOrNull(2L)).isNotNull
        timesheetEntryService.deleteTimesheetEntryForId(2L)
        assertThat(timesheetEntryRepository.findByIdOrNull(2L)).isNull()
    }

    @Test
    fun `delete timesheetEntries removes timesheetEntries from database`() {
        assertThat(timesheetEntryRepository.findByIdOrNull(1L)).isNotNull
        assertThat(timesheetEntryRepository.findByIdOrNull(2L)).isNotNull
        timesheetEntryService.deleteTimesheetEntriesForId(listOf(1, 2))
        assertThat(timesheetEntryRepository.findByIdOrNull(1L)).isNull()
        assertThat(timesheetEntryRepository.findByIdOrNull(2L)).isNull()
    }

    @Test
    fun `getting timesheet entries for timesheetId returns multiple entries`() {
        val entries = timesheetEntryService.findTimesheetEntries(1L, null, null, null)
        assertThat(entries).hasSize(3)
        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry", "Testing timesheet entry2", "Testing timesheet entry8"))
    }

    @Test
    fun `getting timesheet entries for email and dates returns dates in that range for employee`() {
        val startDate = LocalDate.parse("2022-04-01")
        val endDate = LocalDate.parse("2022-04-02")
        val timesheetEntries = timesheetEntryService.findTimesheetEntries(null, "testi@tekija.fi", startDate, endDate)
        assertThat(timesheetEntries).hasSize(1)
    }

    @Test
    fun `getting timesheet entries without proper parameters throws an exception`() {
        assertThatThrownBy { timesheetEntryService.findTimesheetEntries(null, null, null, null) }
            .isInstanceOf(UnableToGetTimesheetEntriesException::class.java)
            .hasMessage("Cannot get timesheetEntries, invalid request parameters")
    }

    @Test
    fun `getting timesheet entries for invalid dates throws an exception`() {
        val startDate = LocalDate.parse("2023-04-01")
        val endDate = LocalDate.parse("2022-04-02")
        assertThatThrownBy { timesheetEntryService.findTimesheetEntries(null, null, startDate, endDate) }
            .isInstanceOf(UnableToGetTimesheetEntriesException::class.java)
            .hasMessage("Cannot get timesheetEntries, invalid request parameters")
    }

    @Test
    fun `getting timesheet entries for dates returns all entries in that range`() {
        val startDate = LocalDate.parse("2022-04-01")
        val endDate = LocalDate.parse("2022-04-02")
        val timesheetEntries = timesheetEntryService.findTimesheetEntries(null, null, startDate, endDate)
        assertThat(timesheetEntries).hasSize(2)
    }

    @Test
    fun `timesheet entries csv export without email filter contains the right rows`() {
        val startDate = LocalDate.parse("2022-04-01")
        val endDate = LocalDate.parse("2022-04-01")
        val csv = timesheetEntryService.exportToCsv(startDate, endDate)
        assertThat(csv.contains("7.5;task;project;2022-04-01;testi@tekija.fi")).isTrue
        assertThat(csv.contains("7.5;task;project;2022-04-01;test@worker.fi")).isTrue
        assertThat(csv.contains("2022-04-02")).isFalse
    }

    @Test
    fun `timesheet entries csv export with email filter contains the right rows`() {
        val email = "testi@tekija.fi"
        val startDate = LocalDate.parse("2022-04-01")
        val endDate = LocalDate.parse("2022-04-03")
        val csv = timesheetEntryService.exportToCsv(startDate, endDate, email)
        assertThat(csv.contains("7.5;task;project;2022-04-01;$email")).isTrue
        assertThat(csv.contains("7.5;task;project;2022-04-03;test@worker.fi")).isFalse
    }

    @Test
    fun `timesheet entries for csv export order exists`() {
        val date = LocalDate.parse("2023-03-01")
        val desc = "Testing timesheet entry5"
        val entry = timesheetEntryService.findTimesheetEntryForId(5)
        assertThat(entry?.description).isEqualTo(desc)
        assertThat(entry?.date).isEqualTo(date)
    }

    @Test
    fun `timesheet entries are sorted when exported to csv`() {
        val startDate = LocalDate.parse("2023-03-01")
        val endDate = LocalDate.parse("2023-03-03")
        val email = "matti@worker.fi"

        val csv = timesheetEntryService.exportToCsv(startDate, endDate, email)
        val csvHeader = "hours;task;project;date;email\n"
        val comparedCsv = csvHeader + listOf(1, 2, 3).joinToString("") { "7.5;task;project;2023-03-0$it;$email\n" }

        assertThat(csv).isEqualTo(comparedCsv)
    }
}
