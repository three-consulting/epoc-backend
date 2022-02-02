package three.consulting.epoc.dto

import three.consulting.epoc.entity.Employee
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class EmployeeDTO(
    val id: Long? = null,
    @field:NotBlank val firstName: String,
    @field:NotBlank val lastName: String,
    @field:NotBlank val email: String,
    val startDate: LocalDate? = null,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null
) {
    constructor(employee: Employee) : this (
        id = employee.id,
        firstName = employee.firstName,
        lastName = employee.lastName,
        email = employee.email,
        startDate = employee.startDate,
        created = employee.created,
        updated = employee.updated
    )
}
