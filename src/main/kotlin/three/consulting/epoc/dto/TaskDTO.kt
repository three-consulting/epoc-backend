package three.consulting.epoc.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import three.consulting.epoc.common.Status
import three.consulting.epoc.entity.Task
import java.time.LocalDateTime

data class TaskDTO(
    val id: Long? = null,
    @field:NotBlank val name: String,
    val description: String? = null,
    @field:NotNull val project: ProjectDTO,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
    @field: NotNull val billable: Boolean = true,
    val status: Status? = Status.ACTIVE,
) {
    constructor(task: Task) : this (
        id = task.id,
        name = task.name,
        description = task.description,
        project = ProjectDTO(task.project),
        created = task.created,
        updated = task.updated,
        billable = task.billable,
        status = task.status,
    )
}
