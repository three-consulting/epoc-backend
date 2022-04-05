package three.consulting.epoc.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import three.consulting.epoc.entity.Timesheet

@Repository
interface TimesheetRepository : JpaRepository<Timesheet, Long> {
    fun findAllByProjectId(projectId: Long): List<Timesheet>

    fun findAllByEmployeeId(employeeId: Long): List<Timesheet>

    fun findAllByProjectIdAndEmployeeId(projectId: Long, employeeId: Long): List<Timesheet>
}
