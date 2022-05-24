package three.consulting.epoc.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import java.time.LocalDate

@ContextConfiguration(classes = [TimesheetEntryRepository::class])
class TimesheetEntryRepositoryIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var timesheetEntryRepository: TimesheetEntryRepository

    @Test
    fun `searching timesheet entries using employee email and dates returns correct entries`() {
        val timesheetEntries = timesheetEntryRepository.findAllByEmployeeEmailAndDates(
            "testi@tekija.fi",
            LocalDate.parse("2022-04-02"),
            LocalDate.parse("2022-04-04")
        )

        assertThat(timesheetEntries).hasSize(1)
    }

    @Test
    fun `searching timesheet entries using a time interval returns correct entries`() {
        val timesheetEntries = timesheetEntryRepository.findAllByDates(
            LocalDate.parse("2022-04-02"),
            LocalDate.parse("2022-04-04")
        )

        assertThat(timesheetEntries).hasSize(2)
    }
}
