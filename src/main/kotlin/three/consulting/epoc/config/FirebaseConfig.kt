package three.consulting.epoc.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import three.consulting.epoc.service.EmployeeService

private val logger = KotlinLogging.logger {}

@Profile("default")
@Configuration
class FirebaseConfig(
    private val employeeService: EmployeeService
) : InitializingBean {
    override fun afterPropertiesSet() {
        setupFirebase()

        logger.info { "Starting to sync users found in Firebase with the database" }

        try {
            val page = FirebaseAuth.getInstance().listUsers(null)

            for (user in page.iterateAll()) {
                try {
                    employeeService.syncFirebaseUser(user.uid, user.email)
                } catch (e: Exception) {
                    logger.error(e) { "Could not sync user: ${user.email}" }
                }
            }
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            logger.error(e) { "Could not set up Firebase" }
        }
    }
}
