package three.consulting.epoc.repository

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.CustomerDTO
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.service.*
import java.time.LocalDate

@ContextConfiguration(classes = [ProjectService::class, CustomerService::class])
class ProjectServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var customerService: CustomerService

    @Autowired
    private lateinit var projectService: ProjectService

    @Test
    fun `searching a project for id return a project`() {
        val project: ProjectDTO = projectService.findProjectForId(1L)!!
        assertThat(project.name).isEqualTo("test")
        assertThat(project.description).isEqualTo("testing")
        assertThat(project.customer!!.id).isEqualTo(1L)
    }
    @Test
    fun `searching a project for invalid id return null`() {
        val project: ProjectDTO? = projectService.findProjectForId(1000L)
        assertThat(project).isNull()
    }

    @Test
    fun `added project is found from the database`() {
        val project = ProjectDTO(
            name = "Sample",
            description = "Sample project",
            startingDate = LocalDate.now(),
            endDate = LocalDate.now()
        )
        val addedProject: ProjectDTO = projectService.createProject(project)
        assertThat(addedProject.name).isEqualTo(project.name)
        assertThat(addedProject.description).isEqualTo(project.description)
        assertThat(addedProject.startingDate).isEqualTo(project.startingDate)
        assertThat(addedProject.endDate).isEqualTo(project.endDate)
    }

    @Test
    fun `adding project with id fails`() {
        val invalidProject = ProjectDTO(
            id = 2,
            name = "asd",
            customer = null,
            managingEmployee = null,
            startingDate = LocalDate.now()
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
            startingDate = LocalDate.now()
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
            customer = null,
            managingEmployee = null,
            startingDate = LocalDate.now()
        )
        Assertions.assertThatThrownBy { projectService.updateProjectForId(invalidProject) }
            .isInstanceOf(UnableToUpdateProjectException::class.java)
            .hasMessage("Cannot update project, missing project id")
    }

    @Test
    fun `delete project removes project from database`() {
        assertThat(projectService.findProjectForId(2L)).isNotNull
        projectService.deleteProject(2L)
        assertThat(projectService.findProjectForId(2L)).isNull()
    }

    @Test
    fun `delete project with non-existing id raise error`() {
        Assertions.assertThatThrownBy { projectService.deleteProject(1000L) }
            .isInstanceOf(UnableToDeleteProjectException::class.java)
            .hasMessage("Cannot delete project, no project found for given id: 1000")
    }
}
