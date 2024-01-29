package three.consulting.epoc.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import java.time.LocalDate

@ContextConfiguration(classes = [TimesheetEntryRepository::class])
class TimesheetEntryRepositoryIntegrationTest : IntegrationTest() {
    @Autowired
    private lateinit var timesheetEntryRepository: TimesheetEntryRepository

    @Test
    fun `searching timesheet entries using employee email and dates returns correct entries`() {
        val timesheetEntries =
            timesheetEntryRepository.findAllByEmployeeEmailAndDates(
                "testi@tekija.fi",
                LocalDate.parse("2022-04-02"),
                LocalDate.parse("2022-04-04")
            )

        assertThat(timesheetEntries).hasSize(1)
    }

    @Test
    fun `searching timesheet entries using a time interval returns correct entries`() {
        val timesheetEntries =
            timesheetEntryRepository.findAllByDates(
                LocalDate.parse("2022-04-02"),
                LocalDate.parse("2022-04-04")
            )

        assertThat(timesheetEntries).hasSize(2)
    }

    @Test
    fun `searching employee flex using valid email`() {
        val employeeFlex =
            timesheetEntryRepository.sumEmployeeFLexByEmail(
                "testi@tekija.fi"
            )
        assertThat(employeeFlex).isEqualTo(4.5f)
    }

    @Test
    fun `searching employee flex using invalid email`() {
        assertThatThrownBy { timesheetEntryRepository.sumEmployeeFLexByEmail("koira@koiralandia.fi") }
            .isInstanceOf(EmptyResultDataAccessException::class.java)
    }

    @Test
    fun `find timesheet entries by all params`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                "test@worker.fi",
                3L,
                2L,
                3L,
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry12"))
        assertThat(entries).hasSize(1)
    }

    @Test
    fun `find timesheet entries by email`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                "test@worker.fi"
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry9", "Testing timesheet entry12"))
        assertThat(entries).hasSize(2)
    }

    @Test
    fun `find timesheet entries by project`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                null,
                3L,
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry11", "Testing timesheet entry12"))
        assertThat(entries).hasSize(2)
    }

    @Test
    fun `find timesheet entries by customer`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                null,
                null,
                2L,
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry11", "Testing timesheet entry12"))
        assertThat(entries).hasSize(2)
    }

    @Test
    fun `find timesheet entries by task`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                null,
                null,
                null,
                3L,
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry11", "Testing timesheet entry12"))
        assertThat(entries).hasSize(2)
    }

    @Test
    fun `find timesheet entries by missing email`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                null,
                3L,
                2L,
                3L,
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry11", "Testing timesheet entry12"))
        assertThat(entries).hasSize(2)
    }

    @Test
    fun `find timesheet entries by missing project`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                "test@worker.fi",
                null,
                2L,
                3L,
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry12"))
        assertThat(entries).hasSize(1)
    }

    @Test
    fun `find timesheet entries by missing customer`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                "test@worker.fi",
                3L,
                null,
                3L,
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry12"))
        assertThat(entries).hasSize(1)
    }

    @Test
    fun `find timesheet entries by missing task`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
                "test@worker.fi",
                3L,
                2L,
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(listOf("Testing timesheet entry12"))
        assertThat(entries).hasSize(1)
    }

    @Test
    fun `find timesheet entries by only dates`() {
        val entries =
            timesheetEntryRepository.findAllByParams(
                LocalDate.parse("2023-04-01"),
                LocalDate.parse("2023-04-05"),
            )

        assertThat(entries.map { it.description }).containsExactlyElementsOf(
            listOf(
                "Testing timesheet entry8",
                "Testing timesheet entry9",
                "Testing timesheet entry10",
                "Testing timesheet entry11",
                "Testing timesheet entry12"
            )
        )
        assertThat(entries).hasSize(5)
    }
}
