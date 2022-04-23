package three.consulting.epoc.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import three.consulting.epoc.entity.Timesheet

@Repository
interface TimesheetRepository : JpaRepository<Timesheet, Long> {
    fun findAllByProjectId(projectId: Long): List<Timesheet>

    fun findAllByEmployeeId(employeeId: Long): List<Timesheet>

    fun findAllByProjectIdAndEmployeeId(projectId: Long, employeeId: Long): List<Timesheet>

    @Query("SELECT t.* FROM timesheet t JOIN employee e ON t.employee_id = e.id WHERE e.email = :email", nativeQuery = true)
    fun findAllByEmployeeEmail(@Param("email") email: String): List<Timesheet>
}
