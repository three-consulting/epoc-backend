package three.consulting.epoc.controller

import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TimesheetEntryDTO
import three.consulting.epoc.service.TimesheetEntryService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/timesheet-entry"])
class TimesheetEntryController(private val timesheetEntryService: TimesheetEntryService) {

    @GetMapping(value = ["/{timesheetEntryId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimesheetEntryForId(@PathVariable timesheetEntryId: Long) =
        timesheetEntryService.findTimesheetEntryForId(timesheetEntryId)

    @GetMapping(consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimesheetEntriesForTimesheetId(@RequestParam timesheetId: Long) =
        timesheetEntryService.findTimesheetEntriesForTimesheetId(timesheetId)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createTimesheetEntry(@Valid @RequestBody timesheetEntry: TimesheetEntryDTO) =
        timesheetEntryService.createTimesheetEntry(timesheetEntry)

    @PutMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateTimesheetEntryForId(@Valid @RequestBody timesheetEntry: TimesheetEntryDTO) =
        timesheetEntryService.updateTimesheetEntryForId(timesheetEntry)

    @DeleteMapping(value = ["/{timesheetEntryId}"], consumes = [ALL_VALUE])
    fun deleteTimesheetEntryForId(@PathVariable timesheetEntryId: Long) =
        timesheetEntryService.deleteTimesheetEntry(timesheetEntryId)
}
