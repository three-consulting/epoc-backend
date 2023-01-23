package three.consulting.epoc.controller

import jakarta.validation.Valid
import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.service.CustomerService

@RestController
@RequestMapping(path = ["/customer"])
class CustomerController(private val customerService: CustomerService) {

    @GetMapping(value = ["/{customerId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getCustomerForId(@PathVariable customerId: Long) = customerService.findCustomerForId(customerId)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createCustomer(
        @Valid @RequestBody
        customer: CustomerDTO
    ) = customerService.createCustomer(customer)

    @PutMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateCustomerForId(
        @Valid @RequestBody
        customer: CustomerDTO
    ) = customerService.updateCustomerForId(customer)

    @DeleteMapping(value = ["/{customerId}"], consumes = [ALL_VALUE])
    fun deleteCustomerForId(@PathVariable customerId: Long) = customerService.deleteCustomer(customerId)

    @GetMapping(consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getAllCustomers() = customerService.findAllCustomers()
}
