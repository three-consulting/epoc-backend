package three.consulting.epoc.controller

import jakarta.validation.Valid
import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.access.prepost.PostFilter
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TimesheetDTO
import three.consulting.epoc.service.TimesheetService

@RestController
@RequestMapping(path = ["/timesheet"])
class TimesheetController(private val timesheetService: TimesheetService) {
    @PostFilter("hasAuthority('ADMIN') or filterObject.status.name() == 'ACTIVE'")
    @GetMapping(consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimesheets(
        @RequestParam projectId: Long? = null,
        @RequestParam employeeId: Long? = null,
        @RequestParam email: String? = null
    ) = timesheetService.findTimesheets(projectId, employeeId, email)

    @GetMapping(value = ["/{timesheetId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimesheetForId(
        @PathVariable timesheetId: Long
    ) = timesheetService.findTimesheetForId(timesheetId)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createTimesheet(
        @Valid @RequestBody
        timesheet: TimesheetDTO
    ) = timesheetService.createTimesheet(timesheet)

    @PutMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateTimesheetForId(
        @Valid @RequestBody
        timesheet: TimesheetDTO
    ) = timesheetService.updateTimesheetForId(timesheet)

    @DeleteMapping(value = ["/{timesheetId}"], consumes = [ALL_VALUE])
    fun deleteTimesheetForId(
        @PathVariable timesheetId: Long
    ) = timesheetService.deleteTimesheet(timesheetId)
}
