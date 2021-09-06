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
}
