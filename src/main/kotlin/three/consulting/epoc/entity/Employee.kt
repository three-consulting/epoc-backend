package three.consulting.epoc.entity

import jakarta.persistence.*
import three.consulting.epoc.common.Role
import three.consulting.epoc.dto.EmployeeDTO
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Employee(
    @field:Column(name = "first_name", nullable = true) var firstName: String? = null,
    @field:Column(name = "last_name", nullable = true) var lastName: String? = null,
    @field:Column(unique = true, nullable = false) var email: String,
    @field:Column(name = "start_date", nullable = false) var startDate: LocalDate = LocalDate.now(),
    @field:Column(nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "firebase_uid", nullable = true) var firebaseUid: String?,
    @field:Enumerated(EnumType.STRING)
    @field:Column(nullable = false)
    var role: Role,
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    constructor(employeeDTO: EmployeeDTO) : this(
        id = employeeDTO.id,
        firstName = employeeDTO.firstName,
        lastName = employeeDTO.lastName,
        email = employeeDTO.email,
        startDate = employeeDTO.startDate ?: LocalDate.now(),
        created = employeeDTO.created ?: LocalDateTime.now(),
        firebaseUid = employeeDTO.firebaseUid,
        role = employeeDTO.role
    )

    constructor(firebaseEmail: String, firebaseUid: String, role: Role) : this(
        email = firebaseEmail,
        startDate = LocalDate.now(),
        created = LocalDateTime.now(),
        firebaseUid = firebaseUid,
        role = role
    )
}
