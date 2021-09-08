package three.consulting.epoc.service

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
    fun updateCustomerForId(id: Long, customerRequest: CustomerDTO): CustomerDTO {
        val existingCustomer = customerRepository.findByIdOrNull(id)
        if (existingCustomer != null) {
            val customer = Customer(customerRequest)
            return CustomerDTO(customerRepository.save(customer))
        }
        else throw UnableToUpdateCustomer()
    }

}
class UnableToCreateCustomerException : RuntimeException("Cannot create a customer with existing id")

class UnableToUpdateCustomer : RuntimeException("No customer found with given id")
