package three.consulting.epoc.controller

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TimesheetEntryDTO
import three.consulting.epoc.service.TimesheetEntryService
import java.time.LocalDate
import javax.validation.Valid

@RestController
class TimesheetEntryController(private val timesheetEntryService: TimesheetEntryService) {

    @PreAuthorize("hasAuthority('ADMIN') or #email == authentication.principal.getClaim(\"email\")")
    @GetMapping(value = ["/timesheet-entry"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimesheetEntries(
        @RequestParam timesheetId: Long? = null,
        @RequestParam email: String? = null,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate? = null,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate? = null
    ) = timesheetEntryService.findTimesheetEntries(timesheetId, email, startDate, endDate)
    @GetMapping(value = ["/timesheet-entry/{timesheetEntryId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimesheetEntryForId(@PathVariable timesheetEntryId: Long) =
        timesheetEntryService.findTimesheetEntryForId(timesheetEntryId)

    @PreAuthorize("hasAuthority('ADMIN') or #timesheetEntry.timesheet.employee.email == authentication.principal.getClaim(\"email\")")
    @PostMapping(value = ["/timesheet-entry"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createTimesheetEntry(@Valid @RequestBody timesheetEntry: TimesheetEntryDTO) =
        timesheetEntryService.createTimesheetEntry(timesheetEntry)

    @PreAuthorize("hasAuthority('ADMIN') or #timesheetEntryService.hasValidEmails(timesheetEntries, authentication.principal.getClaim(\"email\"))")
    @PostMapping(value = ["/timesheet-entries"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createTimesheetEntries(@Valid @RequestBody timesheetEntries: List<TimesheetEntryDTO>) =
        timesheetEntryService.createTimesheetEntries(timesheetEntries)

    @PutMapping(value = ["/timesheet-entry"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateTimesheetEntryForId(@Valid @RequestBody timesheetEntry: TimesheetEntryDTO) =
        timesheetEntryService.updateTimesheetEntryForId(timesheetEntry)

    @DeleteMapping(value = ["/timesheet-entry/{timesheetEntryId}"], consumes = [ALL_VALUE])
    fun deleteTimesheetEntryForId(@PathVariable timesheetEntryId: Long) =
        timesheetEntryService.deleteTimesheetEntry(timesheetEntryId)
}
