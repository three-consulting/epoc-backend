package three.consulting.epoc.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import three.consulting.epoc.entity.Timesheet

@Repository
interface TimesheetRepository : JpaRepository<Timesheet, Long> {
    fun findAllByProjectId(id: Long): List<Timesheet>
}
