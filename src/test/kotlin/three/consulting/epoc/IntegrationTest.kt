package three.consulting.epoc

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase
class IntegrationTest
