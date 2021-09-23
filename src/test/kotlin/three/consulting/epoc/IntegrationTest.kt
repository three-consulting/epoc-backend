package three.consulting.epoc

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@AutoConfigureEmbeddedDatabase
@DirtiesContext
@ActiveProfiles("it")
abstract class IntegrationTest
