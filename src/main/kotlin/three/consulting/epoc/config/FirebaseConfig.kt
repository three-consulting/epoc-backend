package three.consulting.epoc.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import three.consulting.epoc.service.FirebaseService

private val logger = KotlinLogging.logger {}

@Profile("default")
@Configuration
class FirebaseSync(
    private val firebaseService: FirebaseService
) : InitializingBean {
    override fun afterPropertiesSet() {

        logger.info { "Starting to sync users found in Firebase with the database" }

        try {
            val page = FirebaseAuth.getInstance().listUsers(null)

            for (user in page.iterateAll()) {
                try {
                    val syncedUser = firebaseService.syncFirebaseUser(user.uid, user.email, user.customClaims)
                    logger.info { "synced user: $syncedUser" }
                } catch (e: Exception) {
                    logger.error(e) { "Could not sync user: ${user.email}" }
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Could not iterate and sync firebase users" }
        }
    }
}

@Configuration
class FirebaseConfig() {
    @Bean
    fun firebaseAuth(): FirebaseAuth {
        try {
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
            val firebaseApp = FirebaseApp.initializeApp(options)
            return FirebaseAuth.getInstance(firebaseApp)
        } catch (e: Exception) {
            logger.error(e) { "Could not set up Firebase" }
            throw e
        }
    }
}
