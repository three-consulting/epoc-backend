package three.consulting.epoc.entity

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Employee(
    @field:Column(nullable = false) val first_name: String,
    @field:Column(nullable = false) val last_name: String,
    @field:Column(nullable = true) val email: String? = null,
    @field:Column(nullable = false) var start_date: LocalDate = LocalDate.now(),
    @field:Column(nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)
