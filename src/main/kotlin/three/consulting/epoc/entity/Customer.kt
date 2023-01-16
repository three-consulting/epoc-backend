package three.consulting.epoc.entity

import jakarta.persistence.*
import three.consulting.epoc.dto.CustomerDTO
import java.time.LocalDateTime

@Entity
class Customer(
    @field:Column(unique = true, nullable = false) var name: String,
    @field:Column(nullable = true) var description: String? = null,
    @field:Column(nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var enabled: Boolean = true,
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    constructor(customerDTO: CustomerDTO) : this (
        id = customerDTO.id,
        name = customerDTO.name,
        description = customerDTO.description,
        enabled = customerDTO.enabled ?: true,
        created = customerDTO.created ?: LocalDateTime.now()
    )
}
