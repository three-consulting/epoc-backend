package three.consulting.epoc.dto

import three.consulting.epoc.entity.TimeCategory
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class TimeCategoryDTO(
    val id: Long? = null,
    @field:NotBlank val name: String,
    val description: String? = null,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null
) {
    constructor(timeCategory: TimeCategory) : this (
        id = timeCategory.id,
        name = timeCategory.name,
        description = timeCategory.description,
        created = timeCategory.created,
        updated = timeCategory.updated
    )
}
