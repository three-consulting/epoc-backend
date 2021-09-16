package three.consulting.epoc.dto

import three.consulting.epoc.entity.Project
import java.time.LocalDate
import java.time.LocalDateTime

class ProjectDTO(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val startingDate: LocalDate,
    val endDate: LocalDate? = null,
    val customer: CustomerDTO? = null,
    val managingEmployee: EmployeeDTO? = null,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
) {
    constructor(project: Project) : this (
        id = project.id,
        name = project.name,
        description = project.description,
        startingDate = project.startingDate,
        endDate = project.endDate,
        customer = if (project.customer != null) { CustomerDTO(project.customer) } else { null },
        managingEmployee = if (project.managingEmployee != null) { EmployeeDTO(project.managingEmployee) } else { null },
        created = project.created,
        updated = project.updated,
    )
}
