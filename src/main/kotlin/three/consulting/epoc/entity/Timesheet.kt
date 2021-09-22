package three.consulting.epoc.entity

import three.consulting.epoc.dto.TimesheetDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Timesheet(
    @ManyToOne @JoinColumn(name = "project_id", nullable = false) val project: Project,
    @ManyToOne @JoinColumn(name = "employee_id", nullable = false) val employee: Employee,
    @field:Column(name = "name", nullable = false) val name: String,
    @field:Column(name = "description", nullable = true) val description: String? = null,
    @field:Column(name = "allocation", nullable = false) val allocation: Int,
    @field:Column(name = "created", nullable = false) val created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) val updated: LocalDateTime = LocalDateTime.now(),
    @field:Id @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {
    constructor(timesheetDTO: TimesheetDTO) : this (
        id = timesheetDTO.id,
        name = timesheetDTO.name,
        description = timesheetDTO.description,
        allocation = timesheetDTO.allocation,
        created = timesheetDTO.created ?: LocalDateTime.now(),
        project = Project(timesheetDTO.project),
        employee = Employee(timesheetDTO.employee)
    )
}
