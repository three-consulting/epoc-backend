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

    private companion object {
        const val AUTHORITIES_CLAIM_NAME = "role"
    }

    fun syncFirebaseUser(firebaseUid: String, firebaseEmail: String, customClaims: Map<String, Any>): EmployeeDTO? {

        logger.info { "Syncing user: $firebaseEmail" }

        val employee = employeeRepository.findByEmail(firebaseEmail)

        if (employee != null &&
            firebaseUid == employee.firebaseUid &&
            firebaseEmail == employee.email &&
            customClaims[AUTHORITIES_CLAIM_NAME] as String == employee.role.name
        ) {
            return null
        }

        return if (employee == null) {
            val role = if (customClaims[AUTHORITIES_CLAIM_NAME] === null) Role.USER else Role.valueOf(customClaims[AUTHORITIES_CLAIM_NAME] as String)
            val newEmployee = Employee(firebaseEmail, firebaseUid, role)
            firebaseAuth.setCustomUserClaims(firebaseUid, customClaims)
            EmployeeDTO(employeeRepository.save(newEmployee))
        } else {
            employee.firebaseUid = firebaseUid
            firebaseAuth.setCustomUserClaims(firebaseUid, mapOf(AUTHORITIES_CLAIM_NAME to employee.role.name))
            EmployeeDTO(employeeRepository.save(employee))
        }
    }

    fun updateEmployeeAndFirebaseRole(employeeDTO: EmployeeDTO): EmployeeDTO {

        logger.info { "Updating employee with Firebase uid: ${employeeDTO.firebaseUid}" }

        if (employeeDTO.firebaseUid != null) {
            val customClaims = mapOf(AUTHORITIES_CLAIM_NAME to employeeDTO.role.name)
            val employee = employeeRepository.findByFirebaseUid(employeeDTO.firebaseUid)
            if (employee != null) {
                firebaseAuth.setCustomUserClaims(employeeDTO.firebaseUid, customClaims)
                val updatedEmployee = Employee(employeeDTO)
                return EmployeeDTO(employeeRepository.save(updatedEmployee))
            } else {
                throw UnableToUpdateEmployeeAndRoleException()
            }
        } else {
            throw UnableToUpdateEmployeeAndRoleException()
        }
    }

    fun syncAllFirebaseUsers(): List<EmployeeDTO> {

        logger.info { "Starting to sync users found in Firebase with the database" }

        return try {
            val users = firebaseAuth.listUsers(null)
            users.iterateAll().mapNotNull { user ->
                val syncedUser = syncFirebaseUser(user.uid, user.email, user.customClaims)
                logger.info { "synced user: $syncedUser" }
                syncedUser
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to sync employees" }
            throw UnableToSyncFirebaseUsersException()
        }
    }
}
class UnableToUpdateEmployeeAndRoleException : RuntimeException("Cannot update employee.")
class UnableToSyncFirebaseUsersException : RuntimeException("Cannot sync Firebase users.")