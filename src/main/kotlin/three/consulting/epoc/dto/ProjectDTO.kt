package three.consulting.epoc.dto

import three.consulting.epoc.entity.Customer
import three.consulting.epoc.entity.Employee
import three.consulting.epoc.entity.Project
import java.time.LocalDate
import java.time.LocalDateTime

class ProjectDTO(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val startingDate: LocalDate,
    val endDate: LocalDate? = null,
    val customerId: Customer?,
    val managingEmployeeId: Employee?,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
) {
    constructor(project: Project) : this (
        id = project.id,
        name = project.name,
        description = project.description,
        startingDate = project.startingDate,
        endDate = project.endDate,
        customerId = project.customerId,
        managingEmployeeId = project.managingEmployeeId,
        created = project.created,
        updated = project.updated,
    )
}
