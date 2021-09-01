package three.consulting.epoc.repository

import org.springframework.data.jpa.repository.JpaRepository
import three.consulting.epoc.domain.Customer

interface CustomerRepository : JpaRepository<Customer, Long> {}