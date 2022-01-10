package three.consulting.epoc.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.dto.TaskDTO
import java.time.LocalDate

@ContextConfiguration(classes = [TaskService::class])
class TaskServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var taskService: TaskService

    @Test
    fun `searching a task for id return a task`() {
        val task: TaskDTO = taskService.findTaskForId(1L)!!
        assertThat(task.name).isEqualTo("test")
        assertThat(task.description).isEqualTo("testing")
        assertThat(task.project.id).isEqualTo(1L)
    }
    @Test
    fun `searching a task for invalid id return null`() {
        val task: TaskDTO? = taskService.findTaskForId(1000L)
        assertThat(task).isNull()
    }

    @Test
    fun `added task is found from the database`() {
        val task = TaskDTO(
            name = "Sample",
            description = "Sample task",
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
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
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
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
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            project = ProjectDTO(
                id = 100L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(100, "New Project Customer"),
                managingEmployee = EmployeeDTO(100, "New", "Project-worker", "new.project@worker.fi"),
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
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            project = ProjectDTO(
                id = 1L,
                name = "Sample",
                description = "Sample project",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                customer = CustomerDTO(1, "New Project Customer"),
                managingEmployee = EmployeeDTO(1, "New", "Project-worker", "new.project@worker.fi"),
            ),
        )
        Assertions.assertThatThrownBy { taskService.updateTaskForId(invalidTask) }
            .isInstanceOf(UnableToUpdateTaskException::class.java)
            .hasMessage("Cannot update task, missing task id")
    }

    @Test
    fun `delete task removes task from database`() {
        assertThat(taskService.findTaskForId(2L)).isNotNull
        taskService.deleteTask(2L)
        assertThat(taskService.findTaskForId(2L)).isNull()
    }

    @Test
    fun `delete task with non-existing id raise error`() {
        Assertions.assertThatThrownBy { taskService.deleteTask(1000L) }
            .isInstanceOf(UnableToDeleteTaskException::class.java)
            .hasMessage("Cannot delete task, no task found for given id: 1000")
    }

    @Test
    fun `searching task with project id 1 returns array of task objects`() {
        val tasks = taskService.findTaskForProjectId(1L)
        assertThat(tasks.map { it.name }).containsExactlyElementsOf(listOf("test", "test2"))
    }

    @Test
    fun `searching task with project id 99 returns empty array`() {
        val tasks = taskService.findTaskForProjectId(99L)
        assertThat(tasks).hasSize(0)
    }
}
