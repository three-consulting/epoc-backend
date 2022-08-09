package three.consulting.epoc.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.common.Role
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.dto.TaskDTO
import three.consulting.epoc.repository.TaskRepository
import java.time.LocalDate

@ContextConfiguration(classes = [TaskService::class])
class TaskServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var taskService: TaskService

    @Autowired
    private lateinit var taskRepository: TaskRepository

    val newProjectWorkerId1 = EmployeeDTO(
        id = 1,
        firstName = "New",
        lastName = "Project-worker",
        email = "new.project@worker.fi",
        role = Role.USER
    )

    val newProjectWorkerId100 = EmployeeDTO(
        id = 100,
        firstName = "New",
        lastName = "Project-worker",
        email = "new.project@worker.fi",
        role = Role.USER
    )

    @Test
    fun `searching a task for id return a task`() {
        val task: TaskDTO = taskService.findTaskForId(1L)!!
        assertThat(task.name).isEqualTo("test")
        assertThat(task.description).isEqualTo("testing")
        assertThat(task.project.id).isEqualTo(1L)
    }
    @Test
    fun `searching a task for invalid id return null`() {
        Assertions.assertThatThrownBy { taskService.findTaskForId(1000000L) }
            .isInstanceOf(TaskNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Task not found for id: 1000000\"")
    }

    @Test
    fun `added task is found from the database`() {
        val task = TaskDTO(
            name = "Sample",
            description = "Sample task",
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
        )
        val addedTask: TaskDTO = taskService.createTask(task)
        assertThat(addedTask.name).isEqualTo(task.name)
        assertThat(addedTask.description).isEqualTo(task.description)
    }

    @Test
    fun `adding task with id fails`() {
        val invalidTask = TaskDTO(
            id = 2,
            name = "Sample",
            description = "Sample task",
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
        )
        Assertions.assertThatThrownBy { taskService.createTask(invalidTask) }
            .isInstanceOf(UnableToCreateTaskException::class.java)
            .hasMessage("Cannot create a task with existing id")
    }

    @Test
    fun `adding task with non-existing relation fails`() {
        val invalidTask = TaskDTO(
            name = "Sample",
            description = "Sample task",
            project = ProjectDTO(
                id = 100L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(100, "New Project Customer"),
                managingEmployee = newProjectWorkerId100,
            ),
        )
        Assertions.assertThatThrownBy { taskService.createTask(invalidTask) }
            .isInstanceOf(UnableToCreateTaskException::class.java)
            .hasMessage("Cannot create a task with non-existing relation")
    }

    @Test
    fun `update task with id changes updated time`() {
        val existingTask = taskService.findTaskForId(1L)
        if (existingTask != null) {
            val updatedTask = taskService.updateTaskForId(existingTask)
            assertThat(updatedTask.updated).isNotEqualTo(existingTask.updated)
        }
    }

    @Test
    fun `update task without id raises error`() {
        val invalidTask = TaskDTO(
            name = "Sample",
            description = "Sample task",
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = newProjectWorkerId1,
            ),
        )
        Assertions.assertThatThrownBy { taskService.updateTaskForId(invalidTask) }
            .isInstanceOf(UnableToUpdateTaskException::class.java)
            .hasMessage("Cannot update task, missing task id")
    }

    @Test
    fun `delete task removes task from database`() {
        assertThat(taskRepository.findByIdOrNull(2L)).isNotNull
        taskService.deleteTask(2L)
        assertThat(taskRepository.findByIdOrNull(2L)).isNull()
    }

    @Test
    fun `delete task with non-existing id raise error`() {
        Assertions.assertThatThrownBy { taskService.deleteTask(1000L) }
            .isInstanceOf(UnableToDeleteTaskException::class.java)
            .hasMessage("Cannot delete task, no task found for given id: 1000")
    }

    @Test
    fun `searching task with project id 1 returns array of task objects`() {
        val tasks = taskService.findTasks(1L)
        assertThat(tasks.map { it.name }).containsExactlyElementsOf(listOf("test", "test2"))
    }

    @Test
    fun `searching task with project id 99 returns empty array`() {
        val tasks = taskService.findTasks(99L)
        assertThat(tasks).hasSize(0)
    }

    @Test
    fun `searching task without project id returns array of task objects`() {
        val tasks = taskService.findTasks(null)
        assertThat(tasks.map { it.name }).containsExactlyElementsOf(listOf("test", "test2"))
    }
}
