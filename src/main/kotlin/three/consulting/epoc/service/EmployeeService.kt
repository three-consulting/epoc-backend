package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import three.consulting.epoc.common.Status
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.entity.Employee
import three.consulting.epoc.repository.EmployeeRepository

private val logger = KotlinLogging.logger {}

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository
) {

    fun findEmployeeForId(id: Long): EmployeeDTO? {
        logger.info { "Looking for employee with id: $id" }
        val employee: Employee? = employeeRepository.findByIdOrNull(id)
        employee?.let {
            return EmployeeDTO(it)
        }

        logger.info { "No employee found for id: $id" }
        throw EmployeeNotFoundException(id)
    }

    fun createEmployee(employeeRequest: EmployeeDTO): EmployeeDTO {
        logger.info { "Creating new employee" }
        if (employeeRequest.id == null) {
            val employee = Employee(employeeRequest)
            return EmployeeDTO(employeeRepository.save(employee))
        } else {
            val exception = UnableToCreateEmployeeException()
            logger.error(exception) { "Failed creating a new employee" }
            throw exception
        }
    }

    fun updateEmployeeForId(employeeRequest: EmployeeDTO): EmployeeDTO {
        logger.info { "Updating employee with id: ${employeeRequest.id}" }
        if (employeeRequest.id != null) {
            val previousEmployee = findEmployeeForId(employeeRequest.id)
            if (previousEmployee?.status == Status.ARCHIVED && employeeRequest.status == Status.ARCHIVED) {
                val exception = UnableToUpdateEmployeeException("Cannot update archived employee")
                logger.error(exception) { "Failed updating employee" }
                throw exception
            }
            val employee = Employee(employeeRequest)
            return EmployeeDTO(employeeRepository.save(employee))
        } else {
            val exception = UnableToUpdateEmployeeException("Cannot update employee, missing employee id")
            logger.error(exception) { "Cannot update customer" }
            throw exception
        }
    }

    fun deleteEmployee(employeeId: Long) {
        try {
            logger.info { "Deleting employee with id: $employeeId" }
            employeeRepository.deleteById(employeeId)
        } catch (e: EmptyResultDataAccessException) {
            logger.error(e) { "Cannot delete employee" }
            throw UnableToDeleteEmployeeException(employeeId)
        }
    }

    fun findAllEmployees(): List<EmployeeDTO> {
        val employees = employeeRepository.findAll()
        return employees.map { EmployeeDTO(it) }
    }
}

class UnableToCreateEmployeeException : RuntimeException("Cannot create an employee with existing id")
class UnableToUpdateEmployeeException(message: String) : RuntimeException(message)
class UnableToDeleteEmployeeException(id: Long) :
    RuntimeException("Cannot delete employee, no employee found for given id: $id")
class EmployeeNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for id: $id")
