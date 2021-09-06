package three.consulting.epoc.dto

import three.consulting.epoc.entity.Customer
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class CustomerDTO(
    val id: Long,
    @field:NotBlank val name: String,
    val description: String? = null,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
    val enabled: Boolean? = null
) {
    constructor(customer: Customer) : this (
        id = customer.id,
        name = customer.name,
        description = customer.description,
        created = customer.created,
        updated = customer.updated,
        enabled = customer.enabled
    )
}
