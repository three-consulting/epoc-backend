package three.consulting.epoc.controller

import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import three.consulting.epoc.service.CustomerService

@RestController
@RequestMapping(path = ["/customer"])
class CustomerController(private val customerService: CustomerService) {

    @GetMapping(value = ["/{customerId}"], consumes = [ALL_VALUE])
    fun getCustomerForId(@PathVariable customerId: Long) = customerService.findCustomerForId(customerId)
}
