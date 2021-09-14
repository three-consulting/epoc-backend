package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.entity.Employee
import three.consulting.epoc.repository.EmployeeRepository

private val logger = KotlinLogging.logger {}

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {
    fun findEmployeeForId(id: Long): EmployeeDTO? {
        logger.info("Looking for employee with id: $id")
        val employee: Employee? = employeeRepository.findByIdOrNull(id)
        if (employee != null)
            return EmployeeDTO(employee)
        logger.info("No employee found for id: $id")
        return null
    }
}
