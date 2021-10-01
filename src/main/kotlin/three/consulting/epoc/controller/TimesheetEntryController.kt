package three.consulting.epoc.controller

import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TimesheetEntryDTO
import three.consulting.epoc.service.TimesheetEntryService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/timesheet-entry"])
class TimesheetEntryController(private val timesheetEntryService: TimesheetEntryService) {

    @GetMapping(value = ["/{timesheetEntryId}"])
    fun getTimesheetEntryForId(@PathVariable timesheetEntryId: Long) = timesheetEntryService.findTimesheetEntryForId(timesheetEntryId)

    @PostMapping
    fun createTimesheetEntry(@Valid @RequestBody timesheetEntry: TimesheetEntryDTO) = timesheetEntryService.createTimesheetEntry(timesheetEntry)

    @PutMapping
    fun updateTimesheetEntryForId(@Valid @RequestBody timesheetEntry: TimesheetEntryDTO) = timesheetEntryService.updateTimesheetEntryForId(timesheetEntry)

    @DeleteMapping(value = ["/{timesheetEntryId}"])
    fun deleteTimesheetEntryForId(@PathVariable timesheetEntryId: Long) = timesheetEntryService.deleteTimesheetEntry(timesheetEntryId)
}
