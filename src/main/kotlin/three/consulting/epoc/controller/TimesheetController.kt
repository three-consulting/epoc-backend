package three.consulting.epoc.controller

import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TimesheetDTO
import three.consulting.epoc.service.TimesheetService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/timesheet"])
class TimesheetController(private val timesheetService: TimesheetService) {

    @GetMapping(value = ["/{timesheetId}"])
    fun getTimesheetForId(@PathVariable timesheetId: Long) = timesheetService.findTimesheetForId(timesheetId)

    @PostMapping
    fun createTimesheet(@Valid @RequestBody timesheet: TimesheetDTO) = timesheetService.createTimesheet(timesheet)

    @PutMapping
    fun updateTimesheetForId(@Valid @RequestBody timesheet: TimesheetDTO) = timesheetService.updateTimesheetForId(timesheet)

    @DeleteMapping(value = ["/{timesheetId}"])
    fun deleteTimesheetForId(@PathVariable timesheetId: Long) = timesheetService.deleteTimesheet(timesheetId)
}
