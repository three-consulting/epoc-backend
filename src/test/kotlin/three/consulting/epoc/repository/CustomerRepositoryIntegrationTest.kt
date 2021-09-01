package three.consulting.epoc.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.domain.Customer

class CustomerRepositoryIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Test
    fun `searching a customer for id returns a customer`() {
        val customer: Customer = customerRepository.findById(1L).get()

        assertThat(customer.name).isEqualTo("Maurin Makkara Oy")
        assertThat(customer.description).isEqualTo("Get the pile")
    }
}
