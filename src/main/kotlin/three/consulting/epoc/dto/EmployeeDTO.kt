package three.consulting.epoc.dto

import three.consulting.epoc.entity.Employee
import java.time.LocalDate
import java.time.LocalDateTime

data class EmployeeDTO(
    val id: Long? = null,
    val first_name: String,
    val last_name: String,
    val email: String? = null,
    val start_date: LocalDate? = null,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null
) {
    constructor(employee: Employee) : this (
        id = employee.id,
        first_name = employee.first_name,
        last_name = employee.last_name,
        email = employee.email,
        start_date = employee.start_date,
        created = employee.created,
        updated = employee.updated
    )
}
