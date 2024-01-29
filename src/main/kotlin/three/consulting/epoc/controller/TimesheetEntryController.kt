package three.consulting.epoc.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TimesheetEntryDTO
import three.consulting.epoc.service.TimesheetEntryService
import java.time.LocalDate

@RestController
class TimesheetEntryController(private val timesheetEntryService: TimesheetEntryService) {
    @PreAuthorize("hasAuthority('ADMIN') or #email == authentication.principal.getClaim(\"email\")")
    @GetMapping(value = ["/timesheet-entry"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimesheetEntries(
        @RequestParam timesheetId: Long? = null,
        @RequestParam email: String? = null,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        startDate: LocalDate? = null,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        endDate: LocalDate? = null
    ) = timesheetEntryService.findTimesheetEntries(timesheetId, email, startDate, endDate)

    @GetMapping(value = ["/timesheet-entry/{timesheetEntryId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimesheetEntryForId(
        @PathVariable timesheetEntryId: Long
    ) =
        timesheetEntryService.findTimesheetEntryForId(timesheetEntryId)

    @PreAuthorize("hasAuthority('ADMIN') or #email == authentication.principal.getClaim(\"email\")")
    @GetMapping(value = ["/timesheet-entry/flex"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getEmployeeFlex(
        @RequestParam email: String,
    ) = timesheetEntryService.findEmployeeFlexByEmail(email)

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = ["timesheet-entry/csv-export"], consumes = [ALL_VALUE], produces = [TEXT_PLAIN_VALUE])
    fun exportTimesheetEntriesAsCsv(
        response: HttpServletResponse,
        @RequestParam email: String? = null,
        @RequestParam projectId: Long? = null,
        @RequestParam customerId: Long? = null,
        @RequestParam taskId: Long? = null,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        startDate: LocalDate,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        endDate: LocalDate
    ) {
        response.contentType = "text/csv"
        response.characterEncoding = "utf-8"
        val csvString = timesheetEntryService.exportToCsv(startDate, endDate, email, projectId, customerId, taskId)
        response.writer.print(csvString)
    }

    @PreAuthorize("hasAuthority('ADMIN') or @timesheetEntryService.hasValidEmails(#timesheetEntries, authentication.principal.getClaim(\"email\"))")
    @PostMapping(value = ["/timesheet-entry"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createTimesheetEntries(
        @Valid @RequestBody
        timesheetEntries: List<TimesheetEntryDTO>
    ) =
        timesheetEntryService.createTimesheetEntries(timesheetEntries)

    @PutMapping(value = ["/timesheet-entry"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateTimesheetEntriesForId(
        @Valid @RequestBody
        timesheetEntries: List<TimesheetEntryDTO>
    ) =
        timesheetEntryService.updateTimesheetEntriesForId(timesheetEntries)

    @DeleteMapping(value = ["/timesheet-entry"], consumes = [ALL_VALUE])
    fun deleteTimesheetEntriesForId(
        @Valid @RequestBody timesheetIds: List<Long>
    ) =
        timesheetEntryService.deleteTimesheetEntriesForId(timesheetIds)
}
