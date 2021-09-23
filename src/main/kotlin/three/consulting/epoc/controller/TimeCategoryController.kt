package three.consulting.epoc.controller

import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TimeCategoryDTO
import three.consulting.epoc.service.TimeCategoryService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/timeCategory"])
class TimeCategoryController(private val timeCategoryService: TimeCategoryService) {

    @GetMapping(value = ["/{timeCategoryId}"], consumes = [ALL_VALUE])
    fun getTimeCategoryForId(@PathVariable timeCategoryId: Long) = timeCategoryService.findTimeCategoryForId(timeCategoryId)

    @PostMapping
    fun createTimeCategory(@Valid @RequestBody timeCategory: TimeCategoryDTO) = timeCategoryService.createTimeCategory(timeCategory)

    @PutMapping
    fun updateTimeCategoryForId(@Valid @RequestBody timeCategory: TimeCategoryDTO) = timeCategoryService.updateTimeCategoryForId(timeCategory)

    @DeleteMapping(value = ["/{timeCategoryId}"])
    fun deleteTimeCategoryForId(@PathVariable timeCategoryId: Long) = timeCategoryService.deleteTimeCategory(timeCategoryId)

    @GetMapping
    fun getAllTimeCategories() = timeCategoryService.findAllTimeCategories()
}
