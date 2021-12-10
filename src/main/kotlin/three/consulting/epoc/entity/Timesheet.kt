package three.consulting.epoc.entity

import three.consulting.epoc.dto.TimesheetDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["project_id", "employee_id"])])
class Timesheet(
    @ManyToOne @JoinColumn(name = "project_id", nullable = false) var project: Project,
    @ManyToOne @JoinColumn(name = "employee_id", nullable = false) var employee: Employee,
    @field:Column(name = "name", nullable = false) var name: String,
    @field:Column(name = "description", nullable = true) var description: String? = null,
    @field:Column(name = "allocation", nullable = false) var allocation: Int,
    @field:Column(name = "created", nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Id @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
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
