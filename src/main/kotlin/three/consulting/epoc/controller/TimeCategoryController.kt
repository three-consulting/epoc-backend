package three.consulting.epoc.controller

import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TimeCategoryDTO
import three.consulting.epoc.service.TimeCategoryService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/time-category"])
class TimeCategoryController(private val timeCategoryService: TimeCategoryService) {

    @GetMapping(value = ["/{timeCategoryId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTimeCategoryForId(@PathVariable timeCategoryId: Long) = timeCategoryService.findTimeCategoryForId(timeCategoryId)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createTimeCategory(@Valid @RequestBody timeCategory: TimeCategoryDTO) = timeCategoryService.createTimeCategory(timeCategory)

    @PutMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateTimeCategoryForId(@Valid @RequestBody timeCategory: TimeCategoryDTO) = timeCategoryService.updateTimeCategoryForId(timeCategory)

    @DeleteMapping(value = ["/{timeCategoryId}"], consumes = [ALL_VALUE])
    fun deleteTimeCategoryForId(@PathVariable timeCategoryId: Long) = timeCategoryService.deleteTimeCategory(timeCategoryId)

    @GetMapping(consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getAllTimeCategories() = timeCategoryService.findAllTimeCategories()
}
