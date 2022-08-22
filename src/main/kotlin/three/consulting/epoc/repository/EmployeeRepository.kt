package three.consulting.epoc.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import three.consulting.epoc.entity.Employee

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {
    fun findByEmail(email: String): Employee?
    fun findByFirebaseUid(firebaseUid: String): Employee?
}
