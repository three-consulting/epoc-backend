package three.consulting.epoc.service

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.entity.Customer
import three.consulting.epoc.repository.CustomerRepository

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    fun findCustomerForId(id: Long): CustomerDTO? {
        val customer: Customer? = customerRepository.findByIdOrNull(id)
        if (customer != null) {
            return CustomerDTO(customer)
        }
        return null
    }

    fun createCustomer(customerRequest: CustomerDTO): CustomerDTO {
        if (customerRequest.id == null) {
            val customer = Customer(customerRequest)
            return CustomerDTO(customerRepository.save(customer))
        }
        else throw UnableToCreateCustomerException()
    }

    fun updateCustomerForId(customerRequest: CustomerDTO): CustomerDTO {
        if (customerRequest.id != null) {
            val customer = Customer(customerRequest)
            return CustomerDTO(customerRepository.save(customer))
        }
        else throw UnableToUpdateCustomerException()
    }

    fun deleteCustomer(customerId: Long) {
        try {
            customerRepository.deleteById(customerId)
        } catch (e: EmptyResultDataAccessException) {
            throw UnableToDeleteCustomerException(customerId, e)
        }
    }
}
class UnableToCreateCustomerException : RuntimeException("Cannot create a customer with existing id")
class UnableToUpdateCustomerException : RuntimeException("Cannot update customer, missing customer id")
class UnableToDeleteCustomerException(id: Long, exception: EmptyResultDataAccessException) : RuntimeException("Cannot delete customer, no customer found for the given id: $id", exception)
