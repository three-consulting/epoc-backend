package three.consulting.epoc.dto

import three.consulting.epoc.entity.Project
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class ProjectDTO(
    val id: Long? = null,
    @field:NotBlank val name: String,
    val description: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val customer: CustomerDTO,
    val managingEmployee: EmployeeDTO,
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
        created = project.created,
        updated = project.updated,
    )
}
