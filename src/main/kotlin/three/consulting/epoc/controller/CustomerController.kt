package three.consulting.epoc.controller

import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.service.CustomerService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/customer"])
class CustomerController(private val customerService: CustomerService) {

    @GetMapping(value = ["/{customerId}"], consumes = [ALL_VALUE])
    fun getCustomerForId(@PathVariable customerId: Long) = customerService.findCustomerForId(customerId)

    @PostMapping
    fun createCustomer(@Valid @RequestBody customer: CustomerDTO) = customerService.createCustomer(customer)

    @PutMapping
    fun updateCustomerForId(@Valid @RequestBody customer: CustomerDTO) = customerService.updateCustomerForId(customer)

    @DeleteMapping(value = ["/{customerId}"])
    fun deleteCustomerForId(@PathVariable customerId: Long) = customerService.deleteCustomer(customerId)

    @GetMapping
    fun getAllCustomers() = customerService.findAllCustomers()
}
