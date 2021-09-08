package three.consulting.epoc.entity

import three.consulting.epoc.dto.CustomerDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Customer(
    @field:Column(unique = true, nullable = false) val name: String,
    @field:Column(nullable = true) val description: String? = null,
    @field:Column(nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var enabled: Boolean = true,
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
) {
    constructor(customerDTO: CustomerDTO) : this (
        id = customerDTO.id,
        name = customerDTO.name,
        description = customerDTO.description,
        enabled = customerDTO.enabled ?: true,
        created = customerDTO.created ?: LocalDateTime.now()
    )
}
