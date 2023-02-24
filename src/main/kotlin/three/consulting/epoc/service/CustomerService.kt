package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import three.consulting.epoc.common.Status
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.entity.Customer
import three.consulting.epoc.repository.CustomerRepository

private val logger = KotlinLogging.logger {}

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    fun findCustomerForId(id: Long): CustomerDTO? {
        logger.info { "Looking for customer with id: $id" }
        val customer: Customer? = customerRepository.findByIdOrNull(id)
        if (customer != null) {
            return CustomerDTO(customer)
        }
        logger.info { "No customer found for the id: $id" }
        throw CustomerNotFoundException(id)
    }

    fun createCustomer(customerRequest: CustomerDTO): CustomerDTO {
        logger.info("Creating new customer")
        if (customerRequest.id == null) {
            val customer = Customer(customerRequest)
            return CustomerDTO(customerRepository.save(customer))
        } else {
            val exception = UnableToCreateCustomerException()
            logger.error(exception) { "Failed creating a new customer" }
            throw exception
        }
    }

    fun updateCustomerForId(customerRequest: CustomerDTO): CustomerDTO {
        logger.info { "Updating customer with id: ${customerRequest.id}" }
        if (customerRequest.id != null) {
            val previousCustomer = findCustomerForId(customerRequest.id)
            if (previousCustomer?.status == Status.ARCHIVED && customerRequest.status == Status.ARCHIVED) {
                val exception = UnableToUpdateCustomerException("Cannot update archived customer")
                logger.error(exception) { "Failed updating customer" }
                throw exception
            }
            val customer = Customer(customerRequest)
            return CustomerDTO(customerRepository.save(customer))
        } else {
            val exception = UnableToUpdateCustomerException("Cannot update customer, missing customer id")
            logger.error(exception) { "Cannot update customer" }
            throw exception
        }
    }

    fun deleteCustomer(customerId: Long) {
        try {
            logger.info { "Deleting customer with id: $customerId" }
            customerRepository.deleteById(customerId)
        } catch (e: EmptyResultDataAccessException) {
            logger.error(e) { "Cannot delete customer" }
            throw UnableToDeleteCustomerException(customerId)
        }
    }

    fun findAllCustomers(): List<CustomerDTO> {
        val employees = customerRepository.findAll()
        return employees.map { CustomerDTO(it) }
    }
}

class UnableToCreateCustomerException : RuntimeException("Cannot create a customer with existing id")
class UnableToUpdateCustomerException(message: String) : RuntimeException(message)
class UnableToDeleteCustomerException(id: Long) :
    RuntimeException("Cannot delete customer, no customer found for the given id: $id")
class CustomerNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found for id: $id")
