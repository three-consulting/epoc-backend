package three.consulting.epoc.entity

import jakarta.persistence.*
import three.consulting.epoc.dto.TimesheetEntryDTO
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class TimesheetEntry(
    @ManyToOne
    @JoinColumn(name = "timesheet_id", nullable = false)
    var timesheet: Timesheet,
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    var task: Task,
    @field:Column(name = "quantity", nullable = false) var quantity: Float,
    @field:Column(name = "date", nullable = false) var date: LocalDate,
    @field:Column(name = "description", nullable = true) var description: String? = null,
    @field:Column(name = "created", nullable = false) var created: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "updated", nullable = false) var updated: LocalDateTime = LocalDateTime.now(),
    @field:Column(name = "flex", nullable = false) var flex: Float,
    @field:Id
    @field:Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    constructor(timesheetEntryDTO: TimesheetEntryDTO) : this (
        id = timesheetEntryDTO.id,
        quantity = timesheetEntryDTO.quantity,
        date = timesheetEntryDTO.date,
        description = timesheetEntryDTO.description,
        timesheet = Timesheet(timesheetEntryDTO.timesheet),
        task = Task(timesheetEntryDTO.task),
        created = timesheetEntryDTO.created ?: LocalDateTime.now(),
        updated = LocalDateTime.now(),
        flex = timesheetEntryDTO.flex,
    )
}
