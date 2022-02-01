package three.consulting.epoc.entity

import three.consulting.epoc.dto.EmployeeDTO
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Employee(
    @field:Column(name = "first_name", nullable = false) var firstName: String,
    @field:Column(name = "last_name", nullable = false) var lastName: String,
    @field:Column(unique = true, nullable = false) var email: String,
    @field:Column(name = "start_date", nullable = false) var startDate: LocalDate = LocalDate.now(),
    @field:Column(nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
) {
    constructor(employeeDTO: EmployeeDTO) : this (
        id = employeeDTO.id,
        firstName = employeeDTO.firstName,
        lastName = employeeDTO.lastName,
        email = employeeDTO.email,
        startDate = employeeDTO.startDate ?: LocalDate.now(),
        created = employeeDTO.created ?: LocalDateTime.now()
    )
}
