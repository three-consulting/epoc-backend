package three.consulting.epoc.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import three.consulting.epoc.common.Role
import three.consulting.epoc.common.Status
import three.consulting.epoc.entity.Employee
import java.time.LocalDate
import java.time.LocalDateTime

data class EmployeeDTO(
    val id: Long? = null,
    @field:NotBlank val firstName: String?,
    @field:NotBlank val lastName: String?,
    @field:NotBlank val email: String,
    val startDate: LocalDate? = null,
    val status: Status? = Status.ACTIVE,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
    val firebaseUid: String? = null,
    @field:NotNull val role: Role
) {
    constructor(employee: Employee) : this (
        id = employee.id,
        firstName = employee.firstName,
        lastName = employee.lastName,
        email = employee.email,
        startDate = employee.startDate,
        status = employee.status,
        created = employee.created,
        updated = employee.updated,
        firebaseUid = employee.firebaseUid,
        role = employee.role
    )
}
