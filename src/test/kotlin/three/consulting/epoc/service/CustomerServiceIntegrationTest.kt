package three.consulting.epoc.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.repository.CustomerRepository

@ContextConfiguration(classes = [CustomerService::class])
class CustomerServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var customerService: CustomerService

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Test
    fun `searching a customer for id returns a customer`() {
        val customer: CustomerDTO = customerService.findCustomerForId(1L)!!

        assertThat(customer.name).isEqualTo("Maurin Makkara Oy")
        assertThat(customer.description).isEqualTo("Get the pile")
    }

    @Test
    fun `searching a customer for an invalid id returns null`() {
        assertThatThrownBy { customerService.findCustomerForId(1000L) }
            .isInstanceOf(CustomerNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Customer not found for id: 1000\"")
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
        val invalidCustomer = CustomerDTO(1, "Testi Oy", "Innovating new innovation!", enabled = true)
        assertThatThrownBy { customerService.createCustomer(invalidCustomer) }
            .isInstanceOf(UnableToCreateCustomerException::class.java)
            .hasMessage("Cannot create a customer with existing id")
    }

    @Test
    fun `update customer with id changes updated time`() {
        val existingCustomer = customerService.findCustomerForId(1L)
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

    @Test
    fun `delete customer removes customer from the database`() {
        assertThat(customerRepository.findByIdOrNull(2L)).isNotNull
        customerService.deleteCustomer(2L)
        assertThat(customerRepository.findByIdOrNull(2L)).isNull()
    }

    @Test
    fun `delete customer with non-existing id raises error`() {
        assertThatThrownBy { customerService.deleteCustomer(1000L) }
            .isInstanceOf(UnableToDeleteCustomerException::class.java)
            .hasMessage("Cannot delete customer, no customer found for the given id: 1000")
    }

    @Test
    fun `get all customers`() {
        val customers = customerService.findAllCustomers()
        assertThat(customers.map { it.name }).containsExactlyElementsOf(listOf("Maurin Makkara Oy", "Matin Makkara Oy"))
    }
}
