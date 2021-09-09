package three.consulting.epoc.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.service.CustomerService
import three.consulting.epoc.service.UnableToCreateCustomerException
import three.consulting.epoc.service.UnableToUpdateCustomerException

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

    @Test
    fun `added customer is found from database`() {
        val customer = CustomerDTO(
            name = "Älykäs Oy",
            description = "Innovating new innovation"
        )
        val addedCustomer: CustomerDTO = customerService.createCustomer(customer)
        assertThat(addedCustomer.name).isEqualTo(customer.name)
        assertThat(addedCustomer.description).isEqualTo(customer.description)
        assertThat(addedCustomer.enabled).isEqualTo(true)
    }

    @Test
    fun `adding customer with id fails`() {
        val invalidCustomer = CustomerDTO(1,"Testi Oy", "Innovating new innovation!", enabled = true)
        assertThatThrownBy { customerService.createCustomer(invalidCustomer) }
            .isInstanceOf(UnableToCreateCustomerException::class.java)
            .hasMessage("Cannot create a customer with existing id")
    }

    @Test
    fun `update customer with id changes updated time`() {
        val existingCustomer =  customerService.findCustomerForId(1L)
        if (existingCustomer != null) {
            val updatedCustomer = customerService.updateCustomerForId(existingCustomer)
            assertThat(updatedCustomer.updated).isNotEqualTo(existingCustomer.updated)
        }

    }

    @Test
    fun `update customer without id raises error`() {
        val invalidCustomer = CustomerDTO(name = "Failure Ltd")
        assertThatThrownBy { customerService.updateCustomerForId(invalidCustomer) }
            .isInstanceOf(UnableToUpdateCustomerException::class.java)
            .hasMessage("Cannot update customer, missing customer id")
    }
}
