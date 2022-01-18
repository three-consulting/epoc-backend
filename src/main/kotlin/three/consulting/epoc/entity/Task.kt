package three.consulting.epoc.entity

import three.consulting.epoc.common.Status
import three.consulting.epoc.dto.TaskDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Task(
    @ManyToOne @JoinColumn(name = "project_id", nullable = false) var project: Project,
    @field:Column(name = "name", nullable = false) var name: String,
    @field:Column(name = "description", nullable = true) var description: String? = null,
    @field:Column(name = "created", nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false) var status: Status = Status.ACTIVE,
    @field:Id @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    constructor(taskDTO: TaskDTO) : this (
        id = taskDTO.id,
        name = taskDTO.name,
        description = taskDTO.description,
        created = taskDTO.created ?: LocalDateTime.now(),
        project = Project(taskDTO.project),
        status = taskDTO.status
    )
}
