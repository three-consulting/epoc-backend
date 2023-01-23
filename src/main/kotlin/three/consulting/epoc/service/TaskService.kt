package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import three.consulting.epoc.dto.TaskDTO
import three.consulting.epoc.entity.Task
import three.consulting.epoc.repository.TaskRepository

private val logger = KotlinLogging.logger {}

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun findTasks(id: Long?): List<TaskDTO> {
        logger.info { "Looking for tasks with project_id: $id" }
        val tasks = when {
            id != null -> taskRepository.findAllByProjectId(id)
            else -> taskRepository.findAll()
        }
        return tasks.map { TaskDTO(it) }
    }

    fun findTaskForId(id: Long): TaskDTO? {
        logger.info { "Looking for task with id: $id" }
        val task: Task? = taskRepository.findByIdOrNull(id)
        if (task != null) {
            return TaskDTO(task)
        }
        logger.info { "No task found for id: $id" }
        throw TaskNotFoundException(id)
    }

    fun createTask(taskRequest: TaskDTO): TaskDTO {
        logger.info { "Creating new task" }
        if (taskRequest.id == null) {
            val task = Task(taskRequest)
            try {
                return TaskDTO(taskRepository.save(task))
            } catch (e: DataIntegrityViolationException) {
                logger.error(e) { "Failed creating a new task" }
                throw UnableToCreateTaskException("Cannot create a task with non-existing relation")
            }
        } else {
            val exception = UnableToCreateTaskException("Cannot create a task with existing id")
            logger.error(exception) { "Failed creating a new task" }
            throw exception
        }
    }

    fun updateTaskForId(taskRequest: TaskDTO): TaskDTO {
        logger.info { "Updating task with id: ${taskRequest.id}" }
        if (taskRequest.id != null) {
            val task = Task(taskRequest)
            return TaskDTO(taskRepository.save(task))
        } else {
            val exception = UnableToUpdateTaskException()
            logger.error(exception) { "Cannot update customer" }
            throw exception
        }
    }

    fun deleteTask(taskId: Long) {
        try {
            logger.info { "Deleting task with id: $taskId" }
            taskRepository.deleteById(taskId)
        } catch (e: EmptyResultDataAccessException) {
            logger.error(e) { "Cannot delete task" }
            throw UnableToDeleteTaskException(taskId)
        }
    }
}

class UnableToCreateTaskException(message: String) : RuntimeException(message)
class UnableToUpdateTaskException : RuntimeException("Cannot update task, missing task id")
class UnableToDeleteTaskException(id: Long) :
    RuntimeException("Cannot delete task, no task found for given id: $id")
class TaskNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for id: $id")
