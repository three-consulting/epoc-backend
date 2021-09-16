package three.consulting.epoc.entity

import three.consulting.epoc.dto.ProjectDTO
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "project")
@Entity
data class Project(
    @ManyToOne @JoinColumn(name = "customer_id") val customer: Customer? = null,
    @ManyToOne @JoinColumn(name = "employee_id") val managingEmployee: Employee? = null,
    @field:Column(name = "name", nullable = false) val name: String,
    @field:Column(name = "description", nullable = true) val description: String? = null,
    @field:Column(name = "starting_date", nullable = false) val startingDate: LocalDate = LocalDate.now(),
    @field:Column(name = "endDate", nullable = true) val endDate: LocalDate? = null,
    @field:Column(name = "created", nullable = false) val created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) val updated: LocalDateTime = LocalDateTime.now(),

    @field:Id @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {
    constructor(projectDTO: ProjectDTO) : this (
        id = projectDTO.id,
        name = projectDTO.name,
        description = projectDTO.description,
        startingDate = projectDTO.startingDate,
        endDate = projectDTO.endDate,
        created = projectDTO.created ?: LocalDateTime.now(),
        customer = if (projectDTO.customer != null) { Customer(projectDTO.customer) } else { null },
        managingEmployee = if (projectDTO.managingEmployee != null) { Employee(projectDTO.managingEmployee) } else { null },
    )
}
