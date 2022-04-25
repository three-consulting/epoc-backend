package three.consulting.epoc.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import three.consulting.epoc.entity.TimesheetEntry
import java.time.LocalDate

@Repository
interface TimesheetEntryRepository : JpaRepository<TimesheetEntry, Long> {
    fun findAllByTimesheetId(timesheetId: Long): List<TimesheetEntry>

    @Query(
        """
           SELECT te.* from timesheet_entry te 
           LEFT JOIN timesheet t ON te.timesheet_id = t.id 
           JOIN employee e ON e.id = t.employee_id 
           WHERE e.email = :email AND te.date >= :startDate AND te.date <= :endDate
        """,
        nativeQuery = true
    )
    fun findAllByEmployeeEmailAndDates(
        @Param("email") email: String,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<TimesheetEntry>
}
