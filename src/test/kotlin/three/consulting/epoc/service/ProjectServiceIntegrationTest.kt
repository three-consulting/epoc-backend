package three.consulting.epoc.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.common.Role
import three.consulting.epoc.common.Status
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.dto.EmployeeDTO
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.repository.ProjectRepository
import java.time.LocalDate

@ContextConfiguration(classes = [ProjectService::class])
class ProjectServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var projectService: ProjectService

    @Autowired
    private lateinit var projectRepository: ProjectRepository

    val newProjectWorkerId1 = EmployeeDTO(id = 1, firstName = "New", lastName = "Project-worker", email = "new.project@worker.fi", role = Role.USER)
    val updatingProjectWorkerId1 = EmployeeDTO(id = 1, firstName = "Updating", lastName = "Project-Worker", email = "updatingProject@worker.fi", role = Role.USER)
    val failingProjectWorkerId1 = EmployeeDTO(id = 1, firstName = "Failing", lastName = "Project-Worker", email = "failing-worker@project.fi", role = Role.USER)
    val testPersonId100 = EmployeeDTO(id = 100L, firstName = "Test", lastName = "Person", email = "test@person.fi", role = Role.USER)

    @Test
    fun `searching a project for id return a project`() {
        val project: ProjectDTO = projectService.findProjectForId(1L)!!
        assertThat(project.name).isEqualTo("test")
        assertThat(project.description).isEqualTo("testing")
        assertThat(project.customer.id).isEqualTo(1L)
    }

    @Test
    fun `searching a project for invalid id return null`() {
        Assertions.assertThatThrownBy { projectService.findProjectForId(1000L) }
            .isInstanceOf(ProjectNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Project not found for id: 1000\"")
    }

    @Test
    fun `added project is found from the database`() {
        val project = ProjectDTO(
            name = "Sample",
            description = "Sample project",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
            customer = CustomerDTO(1, "New Project Customer"),
            managingEmployee = newProjectWorkerId1,
            status = Status.ARCHIVED,
        )
        val addedProject: ProjectDTO = projectService.createProject(project)
        assertThat(addedProject.name).isEqualTo(project.name)
        assertThat(addedProject.description).isEqualTo(project.description)
        assertThat(addedProject.startDate).isEqualTo(project.startDate)
        assertThat(addedProject.endDate).isEqualTo(project.endDate)
        assertThat(addedProject.status).isEqualTo(project.status)
    }

    @Test
    fun `adding project with id fails`() {
        val invalidProject = ProjectDTO(
            id = 2,
            name = "asd",
            customer = CustomerDTO(1, "Failing Project Customer"),
            managingEmployee = failingProjectWorkerId1,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
        )
        Assertions.assertThatThrownBy { projectService.createProject(invalidProject) }
            .isInstanceOf(UnableToCreateProjectException::class.java)
            .hasMessage("Cannot create a project with existing id")
    }

    @Test
    fun `adding project with non-existing relation fails`() {
        val invalidProject = ProjectDTO(
            name = "asd",
            customer = CustomerDTO(100L, "Non existing company", enabled = true),
            managingEmployee = testPersonId100,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
        )
        Assertions.assertThatThrownBy { projectService.createProject(invalidProject) }
            .isInstanceOf(UnableToCreateProjectException::class.java)
            .hasMessage("Cannot create a project with non-existing relation")
    }

    @Test
    fun `update project with id changes updated time`() {
        val existingProject = projectService.findProjectForId(1L)
        if (existingProject != null) {
            val updatedProject = projectService.updateProjectForId(existingProject)
            assertThat(updatedProject.updated).isNotEqualTo(existingProject.updated)
        }
    }

    @Test
    fun `update project without id raises error`() {
        val invalidProject = ProjectDTO(
            name = "asd",
            customer = CustomerDTO(1, "Updating Project customer"),
            managingEmployee = updatingProjectWorkerId1,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
        )
        Assertions.assertThatThrownBy { projectService.updateProjectForId(invalidProject) }
            .isInstanceOf(UnableToUpdateProjectException::class.java)
            .hasMessage("Cannot update project, missing project id")
    }

    @Test
    fun `delete project removes project from database`() {
        assertThat(projectRepository.findByIdOrNull(2L)).isNotNull
        projectService.deleteProject(2L)
        assertThat(projectRepository.findByIdOrNull(2L)).isNull()
    }

    @Test
    fun `delete project with non-existing id raise error`() {
        Assertions.assertThatThrownBy { projectService.deleteProject(1000L) }
            .isInstanceOf(UnableToDeleteProjectException::class.java)
            .hasMessage("Cannot delete project, no project found for given id: 1000")
    }

    @Test
    fun `get all projects`() {
        val projects = projectService.findAllProjects()
        assertThat(projects.map { it.name }).containsExactlyElementsOf(listOf("test", "test2"))
    }

    @Test
    fun `create project with end date preceding start date raises error`() {
        val invalidProject = ProjectDTO(
            name = "End date precedes start date",
            description = "Project fail",
            startDate = LocalDate.parse("2021-11-11"),
            endDate = LocalDate.parse("2011-11-11"),
            customer = CustomerDTO(1, "Failing Project Customer"),
            managingEmployee = failingProjectWorkerId1,
        )
        Assertions.assertThatThrownBy { projectService.createProject(invalidProject) }
            .isInstanceOf(UnableToCreateProjectException::class.java)
            .hasMessage("Cannot create a project with end date preceding start date.")
    }

    @Test
    fun `create project with null end date`() {
        val project = ProjectDTO(
            name = "Sample",
            description = "Sample project with null end date",
            startDate = LocalDate.now(),
            endDate = null,
            customer = CustomerDTO(1, "New Project Customer"),
            managingEmployee = newProjectWorkerId1,
            status = Status.ARCHIVED,
        )
        val addedProject: ProjectDTO = projectService.createProject(project)
        assertThat(addedProject.name).isEqualTo(project.name)
        assertThat(addedProject.description).isEqualTo(project.description)
        assertThat(addedProject.startDate).isEqualTo(project.startDate)
        assertThat(addedProject.endDate).isEqualTo(project.endDate)
        assertThat(addedProject.status).isEqualTo(project.status)
    }

    @Test
    fun `update project with end date preceding start date raises error`() {
        val invalidProject = ProjectDTO(
            id = 1,
            name = "asd",
            customer = CustomerDTO(1, "Updating Project customer"),
            managingEmployee = updatingProjectWorkerId1,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().minusDays(1),
        )
        Assertions.assertThatThrownBy { projectService.updateProjectForId(invalidProject) }
            .isInstanceOf(UnableToUpdateProjectException::class.java)
            .hasMessage("Cannot update a project with end date preceding start date.")
    }

    @Test
    fun `update project with null end date passes`() {
        val existingProject = projectService.findProjectForId(1)
        val project = ProjectDTO(
            id = 1,
            name = "asd",
            customer = CustomerDTO(1, "Updating Project customer"),
            managingEmployee = updatingProjectWorkerId1,
            startDate = LocalDate.now(),
            endDate = null,
        )
        val updatedProject = projectService.updateProjectForId(project)
        assertThat(updatedProject.name).isNotEqualTo(existingProject?.name)
    }

    @Test
    fun `update project with start date preceding end date passes`() {
        val existingProject = projectService.findProjectForId(1)
        val project = ProjectDTO(
            id = 1,
            name = "asd",
            customer = CustomerDTO(1, "Updating Project customer"),
            managingEmployee = updatingProjectWorkerId1,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
        )
        val updatedProject = projectService.updateProjectForId(project)
        assertThat(updatedProject.updated).isNotEqualTo(existingProject?.updated)
    }
}
