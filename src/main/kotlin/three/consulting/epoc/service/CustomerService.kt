package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.entity.Customer
import three.consulting.epoc.repository.CustomerRepository

private val logger = KotlinLogging.logger {}

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    fun findCustomerForId(id: Long): CustomerDTO? {
        logger.info("Looking for customer with id: $id")
        val customer: Customer? = customerRepository.findByIdOrNull(id)
        if (customer != null) {
            return CustomerDTO(customer)
        }
        logger.info("No customer found for the id: $id")
        return null
    }

    fun createCustomer(customerRequest: CustomerDTO): CustomerDTO {
        logger.info("Creating new customer")
        if (customerRequest.id == null) {
            val customer = Customer(customerRequest)
            return CustomerDTO(customerRepository.save(customer))
        } else {
            val exception = UnableToCreateCustomerException()
            logger.error("Cannot create new customer", exception)
            throw exception
        }
    }

    fun updateCustomerForId(customerRequest: CustomerDTO): CustomerDTO {
        logger.info("Updating customer")
        if (customerRequest.id != null) {
            logger.debug("Updating customer with id: ${customerRequest.id}")
            val customer = Customer(customerRequest)
            return CustomerDTO(customerRepository.save(customer))
        } else {
            val exception = UnableToUpdateCustomerException()
            logger.error("Cannot update customer", exception)
            throw exception
        }
    }

    fun deleteCustomer(customerId: Long) {
        logger.info("Deleting customer")
        try {
            logger.debug("Deleting customer with id: $customerId")
            customerRepository.deleteById(customerId)
        } catch (e: EmptyResultDataAccessException) {
            val exception = UnableToDeleteCustomerException(customerId, e)
            logger.error("Cannot delete customer", exception)
            throw exception
        }
    }
}

class UnableToCreateCustomerException : RuntimeException("Cannot create a customer with existing id")
class UnableToUpdateCustomerException : RuntimeException("Cannot update customer, missing customer id")
class UnableToDeleteCustomerException(id: Long, exception: EmptyResultDataAccessException) :
    RuntimeException("Cannot delete customer, no customer found for the given id: $id", exception)
