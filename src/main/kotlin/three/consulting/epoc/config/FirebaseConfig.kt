package three.consulting.epoc.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

private val logger = KotlinLogging.logger {}

@Profile("default")
@Configuration
class FirebaseConfig(
    @Value("\${FIREBASE_SERVICE_ACCOUNT_JSON}") private val firebaseServiceAccountJson: String
) {
    @Bean
    fun firebaseAuth(): FirebaseAuth =
        try {
            val serviceAccount = firebaseServiceAccountJson.byteInputStream()

            val options =
                FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()

            val firebaseApp = FirebaseApp.initializeApp(options)
            FirebaseAuth.getInstance(firebaseApp)
        } catch (e: Exception) {
            logger.error(e) { "Could not set up Firebase" }
            throw e
        }
}
