package three.consulting.epoc.controller

import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.service.EmployeeService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/employee"])
class EmployeeController(private val employeeService: EmployeeService) {

    @GetMapping(value = ["/{employeeId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getEmployeeForId(@PathVariable employeeId: Long) = employeeService.findEmployeeForId(employeeId)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createEmployee(@Valid @RequestBody employee: EmployeeDTO) = employeeService.createEmployee(employee)

    @PutMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateEmployeeForId(@Valid @RequestBody employee: EmployeeDTO) = employeeService.updateEmployeeForId(employee)

    @DeleteMapping(value = ["/{employeeId}"], consumes = [ALL_VALUE])
    fun deleteEmployeeForId(@PathVariable employeeId: Long) = employeeService.deleteEmployee(employeeId)

    @GetMapping(consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getAllEmployees() = employeeService.findAllEmployees()
}
