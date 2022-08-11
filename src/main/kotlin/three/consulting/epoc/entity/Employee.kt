package three.consulting.epoc.entity

import com.google.firebase.auth.ExportedUserRecord
import three.consulting.epoc.common.Role
import three.consulting.epoc.dto.EmployeeDTO
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Employee(
    @field:Column(name = "first_name", nullable = true) var firstName: String? = null,
    @field:Column(name = "last_name", nullable = true) var lastName: String? = null,
    @field:Column(unique = true, nullable = false) var email: String,
    @field:Column(name = "start_date", nullable = false) var startDate: LocalDate = LocalDate.now(),
    @field:Column(nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "firebase_uid", nullable = true) var firebaseUid: String?,
    @field:Column(nullable = false) var role: Role,
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
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

    constructor(firebaseUser: ExportedUserRecord) : this(
        email = firebaseUser.email,
        startDate = LocalDate.now(),
        created = LocalDateTime.now(),
        firebaseUid = firebaseUser.uid,
        role = Role.USER
    )
}
