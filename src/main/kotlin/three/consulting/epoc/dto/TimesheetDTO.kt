package three.consulting.epoc.dto

import three.consulting.epoc.common.Status
import three.consulting.epoc.entity.Timesheet
import java.time.LocalDateTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TimesheetDTO(
    val id: Long? = null,
    @field:NotBlank val name: String,
    val description: String? = null,
    @field:Min(0) @field:NotNull val rate: Float,
    @field:Min(0) @field:Max(100) val allocation: Int,
    @field:NotNull val project: ProjectDTO,
    @field:NotNull val employee: EmployeeDTO,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
    val status: Status = Status.ACTIVE,
) {
    constructor(timesheet: Timesheet) : this (
        id = timesheet.id,
        name = timesheet.name,
        description = timesheet.description,
        rate = timesheet.rate,
        allocation = timesheet.allocation,
        project = ProjectDTO(timesheet.project),
        employee = EmployeeDTO(timesheet.employee),
        created = timesheet.created,
        updated = timesheet.updated,
        status = timesheet.status
    )
}
