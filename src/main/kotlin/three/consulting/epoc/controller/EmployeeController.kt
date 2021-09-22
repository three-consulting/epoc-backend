package three.consulting.epoc.controller

import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.service.EmployeeService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/employee"])
class EmployeeController(private val employeeService: EmployeeService) {

    @GetMapping(value = ["/{employeeId}"])
    fun getEmployeeForId(@PathVariable employeeId: Long) = employeeService.findEmployeeForId(employeeId)

    @PostMapping
    fun createEmployee(@Valid @RequestBody employee: EmployeeDTO) = employeeService.createEmployee(employee)

    @PutMapping
    fun updateEmployeeForId(@Valid @RequestBody employee: EmployeeDTO) = employeeService.updateEmployeeForId(employee)

    @DeleteMapping(value = ["/{employeeId}"])
    fun deleteEmployeeForId(@PathVariable employeeId: Long) = employeeService.deleteEmployee(employeeId)
}
