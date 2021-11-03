package three.consulting.epoc.entity

import three.consulting.epoc.dto.TaskDTO
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Task(
    @ManyToOne @JoinColumn(name = "project_id", nullable = false) var project: Project,
    @field:Column(name = "name", nullable = false) var name: String,
    @field:Column(name = "description", nullable = true) var description: String? = null,
    @field:Column(name = "start_date", nullable = false) var startDate: LocalDate = LocalDate.now(),
    @field:Column(name = "end_date", nullable = true) var endDate: LocalDate? = null,
    @field:Column(name = "created", nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Id @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
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
