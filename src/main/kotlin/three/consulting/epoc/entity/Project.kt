package three.consulting.epoc.entity

import three.consulting.epoc.common.Status
import three.consulting.epoc.dto.ProjectDTO
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Project(
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    var customer: Customer,
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    var managingEmployee: Employee,
    @field:Column(name = "name", nullable = false) var name: String,
    @field:Column(name = "description", nullable = true) var description: String? = null,
    @field:Column(name = "start_date", nullable = false) var startDate: LocalDate = LocalDate.now(),
    @field:Column(name = "end_date", nullable = true) var endDate: LocalDate? = null,
    @field:Column(name = "created", nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: Status = Status.ACTIVE,
    @field:Id
    @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
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
