package three.consulting.epoc.dto

import three.consulting.epoc.entity.Task
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class TaskDTO(
    val id: Long? = null,
    @field:NotBlank val name: String,
    val description: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val project: ProjectDTO,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
) {
    constructor(task: Task) : this (
        id = task.id,
        name = task.name,
        description = task.description,
        startDate = task.startDate,
        endDate = task.endDate,
        project = ProjectDTO(task.project),
        created = task.created,
        updated = task.updated,
    )
}
