package three.consulting.epoc.dto

import three.consulting.epoc.common.Status
import three.consulting.epoc.entity.Project
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ProjectDTO(
    val id: Long? = null,
    @field:NotBlank val name: String,
    val description: String? = null,
    @field:NotNull val startDate: LocalDate,
    val endDate: LocalDate? = null,
    @field:NotNull val customer: CustomerDTO,
    @field:NotNull val managingEmployee: EmployeeDTO,
    val status: Status = Status.ACTIVE,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
) {
    constructor(project: Project) : this (
        id = project.id,
        name = project.name,
        description = project.description,
        startDate = project.startDate,
        endDate = project.endDate,
        customer = CustomerDTO(project.customer),
        managingEmployee = EmployeeDTO(project.managingEmployee),
        status = project.status,
        created = project.created,
        updated = project.updated,
    )
}
