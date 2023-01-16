package three.consulting.epoc.controller

import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TaskDTO
import three.consulting.epoc.service.TaskService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/task"])
class TaskController(private val taskService: TaskService) {

    @GetMapping(consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTasks(@RequestParam projectId: Long?) = taskService.findTasks(projectId)

    @GetMapping(value = ["/{taskId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getTaskForId(@PathVariable taskId: Long) = taskService.findTaskForId(taskId)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createTask(
        @Valid @RequestBody
        task: TaskDTO
    ) = taskService.createTask(task)

    @PutMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateTaskForId(
        @Valid @RequestBody
        task: TaskDTO
    ) = taskService.updateTaskForId(task)

    @DeleteMapping(value = ["/{taskId}"], consumes = [ALL_VALUE])
    fun deleteTaskForId(@PathVariable taskId: Long) = taskService.deleteTask(taskId)
}
