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
import three.consulting.epoc.entity.Employee
import three.consulting.epoc.repository.EmployeeRepository

private val logger = KotlinLogging.logger {}

@Profile("default")
@Configuration
class FirebaseConfig(
    private val employeeRepository: EmployeeRepository
) : InitializingBean {
    override fun afterPropertiesSet() {
        logger.info { "Setting up Firebase" }
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()

        FirebaseApp.initializeApp(options)

        logger.info { "Starting to sync users found in Firebase with the database" }

        val page = FirebaseAuth.getInstance().listUsers(null)
        for (user in page.iterateAll()) {
            syncUser(user)
        }
    }

    private fun syncUser(user: ExportedUserRecord) {
        logger.info { "Syncing user: ${user.email}" }

        val employee = employeeRepository.findByEmail(user.email)

        if (employee == null) {
            val newEmployee = Employee(
                firstName = "",
                lastName = "",
                email = user.email,
                // uid = user.uid
            )
            logger.info { "Saving new employee ${user.email}" }
            employeeRepository.save(newEmployee)
        }
        // set uid for existing employees
    }
}
