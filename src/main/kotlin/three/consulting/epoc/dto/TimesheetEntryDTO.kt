package three.consulting.epoc.dto

import three.consulting.epoc.entity.TimesheetEntry
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class TimesheetEntryDTO(
    val id: Long? = null,
    @field:NotNull val quantity: Duration,
    @field:NotNull val date: LocalDate,
    val description: String? = null,
    @field:NotNull val timesheet: TimesheetDTO,
    @field:NotNull val timeCategory: TimeCategoryDTO,
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
        timeCategory = TimeCategoryDTO(timesheetEntry.timeCategory),
        task = TaskDTO(timesheetEntry.task),
        created = timesheetEntry.created,
        updated = timesheetEntry.updated,
    )
}
