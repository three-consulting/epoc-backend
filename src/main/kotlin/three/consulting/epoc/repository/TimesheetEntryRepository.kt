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

    @Query(
        """
           SELECT te.* from timesheet_entry te 
           WHERE te.date >= :startDate AND te.date <= :endDate
        """,
        nativeQuery = true
    )
    fun findAllByDates(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<TimesheetEntry>

    @Query(
        """
            SELECT te.* from timesheet_entry te
            LEFT JOIN timesheet t ON t.id = te.timesheet_id
            LEFT JOIN task tk ON tk.id = te.task_id
            LEFT JOIN employee e ON e.id = t.employee_id
            LEFT JOIN project p ON p.id = t.project_id
            LEFT JOIN customer c ON c.id =  p.customer_id
            WHERE (te.date >= :startDate AND te.date <= :endDate)
                AND (:email is null or e.email = :email)
                AND (:projectId is null or p.id = :projectId)
                AND (:customerId is null or c.id = :customerId)
                AND (:taskId is null or tk.id = :taskId)
        """,
        nativeQuery = true
    )
    fun findAllByParams(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("email") email: String? = null,
        @Param("projectId") projectId: Long? = null,
        @Param("customerId") customerId: Long? = null,
        @Param("taskId") taskId: Long? = null,
    ): List<TimesheetEntry>
}
