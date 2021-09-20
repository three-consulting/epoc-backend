package three.consulting.epoc.entity

import three.consulting.epoc.dto.ProjectDTO
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

enum class Status {
    ACTIVE, INACTIVE, ARCHIVED
}

@Entity
data class Project(
    @ManyToOne @JoinColumn(name = "customer_id", nullable = false) val customer: Customer,
    @ManyToOne @JoinColumn(name = "employee_id", nullable = false) val managingEmployee: Employee,
    @field:Column(name = "name", nullable = false) val name: String,
    @field:Column(name = "description", nullable = true) val description: String? = null,
    @field:Column(name = "start_date", nullable = false) val startDate: LocalDate = LocalDate.now(),
    @field:Column(name = "end_date", nullable = true) val endDate: LocalDate? = null,
    @field:Column(name = "created", nullable = false) val created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) val updated: LocalDateTime = LocalDateTime.now(),
    @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false) val status: Status = Status.ACTIVE,
    @field:Id @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {
    constructor(projectDTO: ProjectDTO) : this (
        id = projectDTO.id,
        name = projectDTO.name,
        description = projectDTO.description,
        startDate = projectDTO.startDate,
        endDate = projectDTO.endDate,
        created = projectDTO.created ?: LocalDateTime.now(),
        customer = Customer(projectDTO.customer),
        managingEmployee = Employee(projectDTO.managingEmployee),
        status = projectDTO.status
    )
}
