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

    @PostMapping()
    fun createCustomer(@Valid @RequestBody customer: CustomerDTO) = customerService.createCustomer(customer)

    @PutMapping(value = ["/{customerId}"])
    fun updateCustomerForId(@PathVariable customerId: Long, @Valid @RequestBody customer: CustomerDTO) = customerService.updateCustomerForId(customerId, customer)
}
