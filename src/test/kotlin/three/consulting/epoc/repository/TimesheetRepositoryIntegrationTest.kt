package three.consulting.epoc.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest

@ContextConfiguration(classes = [TimesheetRepository::class])
class TimesheetRepositoryIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var timesheetRepository: TimesheetRepository

    @Test
    fun `searching a timesheet using an employee email returns timesheets for that employee`() {
        val timesheets = timesheetRepository.findAllByEmployeeEmail("testi@tekija.fi")
        assertThat(timesheets).hasSize(2)
    }
}
