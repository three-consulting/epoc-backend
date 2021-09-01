package three.consulting.epoc.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.service.CustomerService

@ContextConfiguration(classes = [CustomerService::class])
class CustomerServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var customerService: CustomerService

    @Test
    fun `searching a customer for id returns a customer`() {
        val customer: CustomerDTO = customerService.findCustomerForId(1L)!!

        assertThat(customer.name).isEqualTo("Maurin Makkara Oy")
        assertThat(customer.description).isEqualTo("Get the pile")
    }

    @Test
    fun `searching a customer for an invalid id returns null`() {
        val customer: CustomerDTO? = customerService.findCustomerForId(1000L)
        assertThat(customer).isNull()
    }
}
