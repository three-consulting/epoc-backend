package three.consulting.epoc.controller

import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.service.EmployeeService
import three.consulting.epoc.service.FirebaseService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/employee"])
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = ["/{employeeId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getEmployeeForId(@PathVariable employeeId: Long) = employeeService.findEmployeeForId(employeeId)

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createEmployee(@Valid @RequestBody employee: EmployeeDTO) = employeeService.createEmployee(employee)

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateEmployeeForId(@Valid @RequestBody employee: EmployeeDTO) = employeeService.updateEmployeeForId(employee)

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = ["/{employeeId}"], consumes = [ALL_VALUE])
    fun deleteEmployeeForId(@PathVariable employeeId: Long) = employeeService.deleteEmployee(employeeId)

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getAllEmployees() = employeeService.findAllEmployees()
}

@Profile("default")
@RestController
@RequestMapping(path = ["/employee"])
class FirebaseEmployeeController(
    private val firebaseService: FirebaseService
) {
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = ["/update-role"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateEmployeeRole(@Valid @RequestBody employee: EmployeeDTO) = firebaseService.updateFirebaseUserRole(employee)
}
