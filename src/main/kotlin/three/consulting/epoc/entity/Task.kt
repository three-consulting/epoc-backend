package three.consulting.epoc.entity

import three.consulting.epoc.dto.TaskDTO
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Task(
    @ManyToOne @JoinColumn(name = "project_id", nullable = false) val project: Project,
    @field:Column(name = "name", nullable = false) val name: String,
    @field:Column(name = "description", nullable = true) val description: String? = null,
    @field:Column(name = "start_date", nullable = false) val startDate: LocalDate = LocalDate.now(),
    @field:Column(name = "end_date", nullable = true) val endDate: LocalDate? = null,
    @field:Column(name = "created", nullable = false) val created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) val updated: LocalDateTime = LocalDateTime.now(),
    @field:Id @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {
    constructor(taskDTO: TaskDTO) : this (
        id = taskDTO.id,
        name = taskDTO.name,
        description = taskDTO.description,
        startDate = taskDTO.startDate,
        endDate = taskDTO.endDate,
        created = taskDTO.created ?: LocalDateTime.now(),
        project = Project(taskDTO.project),
    )
}
