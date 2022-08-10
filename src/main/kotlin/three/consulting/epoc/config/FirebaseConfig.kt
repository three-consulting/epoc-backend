package three.consulting.epoc.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.ExportedUserRecord
import com.google.firebase.auth.FirebaseAuth
import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import three.consulting.epoc.common.Role
import three.consulting.epoc.entity.Employee
import three.consulting.epoc.repository.EmployeeRepository

private val logger = KotlinLogging.logger {}

@Profile("default")
@Configuration
class FirebaseConfig(
    private val employeeRepository: EmployeeRepository
) : InitializingBean {
    override fun afterPropertiesSet() {
        setupFirebase()

        logger.info { "Starting to sync users found in Firebase with the database" }

        try {
            val page = FirebaseAuth.getInstance().listUsers(null)

            for (user in page.iterateAll()) {
                try {
                    syncUser(user)
                } catch (e: Error) {
                    logger.error(e) { "Could not sync user: ${user.email}" }
                }
            }
        } catch (e: Error) {
            logger.error(e) { "Could not iterate and sync firebase users" }
        }
    }

    private fun setupFirebase() {
        try {
            logger.info { "Setting up Firebase" }

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()

            FirebaseApp.initializeApp(options)
        } catch (e: Error) {
            logger.error(e) { "Could not set up Firebase" }
        }
    }

    private fun syncUser(userRecord: ExportedUserRecord) {
        logger.info { "Syncing user: ${userRecord.email}" }

        val employee = employeeRepository.findByEmail(userRecord.email)

        if (employee == null) {
            val newEmployee = Employee(userRecord)

            newEmployee.role = Role.USER
            logger.info { "Saving new employee ${userRecord.email}" }

            employeeRepository.save(newEmployee)
        } else {
            employee.firebaseUid = userRecord.uid
            employeeRepository.save(employee)
        }
    }
}
