package three.consulting.epoc.dto

import jakarta.validation.constraints.NotNull
import three.consulting.epoc.entity.TimesheetEntry
import java.time.LocalDate
import java.time.LocalDateTime

data class TimesheetEntryDTO(
    val id: Long? = null,
    @field:NotNull val quantity: Float,
    @field:NotNull val date: LocalDate,
    val description: String? = null,
    @field:NotNull val timesheet: TimesheetDTO,
    @field:NotNull val task: TaskDTO,
    val created: LocalDateTime? = null,
    val updated: LocalDateTime? = null,
) {
    constructor(timesheetEntry: TimesheetEntry) : this (
        id = timesheetEntry.id,
        quantity = timesheetEntry.quantity,
        date = timesheetEntry.date,
        description = timesheetEntry.description,
        timesheet = TimesheetDTO(timesheetEntry.timesheet),
        task = TaskDTO(timesheetEntry.task),
        created = timesheetEntry.created,
        updated = timesheetEntry.updated,
    )
}
