package three.consulting.epoc.entity

import three.consulting.epoc.dto.TimeCategoryDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class TimeCategory(
    @field:Column(unique = true, nullable = false) var name: String,
    @field:Column(nullable = true) var description: String? = null,
    @field:Column(nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
) {
    constructor(timeCategoryDTO: TimeCategoryDTO) : this (
        id = timeCategoryDTO.id,
        name = timeCategoryDTO.name,
        description = timeCategoryDTO.description,
        created = timeCategoryDTO.created ?: LocalDateTime.now()
    )
}
