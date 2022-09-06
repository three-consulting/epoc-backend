package three.consulting.epoc.service

import com.google.firebase.auth.FirebaseAuth
import mu.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import three.consulting.epoc.common.Role
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.entity.Employee
import three.consulting.epoc.repository.EmployeeRepository

private val logger = KotlinLogging.logger {}

@Profile("default")
@Service
class FirebaseService(
    private val employeeRepository: EmployeeRepository,
    private val firebaseAuth: FirebaseAuth
) {

    fun syncFirebaseUser(firebaseUid: String, firebaseEmail: String, customClaims: Map<String, Any>): EmployeeDTO {

        logger.info { "Syncing user: $firebaseEmail" }

        val employee = employeeRepository.findByEmail(firebaseEmail)

        return if (employee == null) {
            val role = if (customClaims["role"] === null) Role.USER else Role.valueOf(customClaims["role"] as String)
            val newEmployee = Employee(firebaseEmail, firebaseUid, role)
            firebaseAuth.setCustomUserClaims(firebaseUid, customClaims)
            EmployeeDTO(employeeRepository.save(newEmployee))
        } else {
            employee.firebaseUid = firebaseUid
            firebaseAuth.setCustomUserClaims(firebaseUid, mapOf("role" to employee.role.name))
            EmployeeDTO(employeeRepository.save(employee))
        }
    }

    fun updateEmployeeAndFirebaseRole(employeeDTO: EmployeeDTO) {
        if (employeeDTO.firebaseUid != null) {
            val customClaims = mapOf("role" to employeeDTO.role.name)
            val employee = employeeRepository.findByFirebaseUid(employeeDTO.firebaseUid)
            if (employee != null) {
                firebaseAuth.setCustomUserClaims(employeeDTO.firebaseUid, customClaims)
                employee.role = employeeDTO.role
                employeeRepository.save(employee)
            }
        } else {
            throw UnableToUpdateUserRoleException()
        }
    }
}
class UnableToUpdateUserRoleException : RuntimeException("Cannot update employee.")
