package three.consulting.epoc.controller

import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.TaskDTO
import three.consulting.epoc.service.TaskService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/task"])
class TaskController(private val taskService: TaskService) {

    @GetMapping(value = ["/{taskId}"])
    fun getTaskForId(@PathVariable taskId: Long) = taskService.findTaskForId(taskId)

    @PostMapping
    fun createTask(@Valid @RequestBody task: TaskDTO) = taskService.createTask(task)

    @PutMapping
    fun updateTaskForId(@Valid @RequestBody task: TaskDTO) = taskService.updateTaskForId(task)

    @DeleteMapping(value = ["/{taskId}"])
    fun deleteTaskForId(@PathVariable taskId: Long) = taskService.deleteTask(taskId)
}
