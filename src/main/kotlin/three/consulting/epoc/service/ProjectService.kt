package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.entity.Project
import three.consulting.epoc.repository.ProjectRepository

private val logger = KotlinLogging.logger {}

@Service
class ProjectService(private val projectRepository: ProjectRepository) {

    fun findProjectForId(id: Long): ProjectDTO? {
        logger.info { "Looking for project with id: $id" }
        val project: Project? = projectRepository.findByIdOrNull(id)
        project?.run {
            return ProjectDTO(project)
        }

        logger.info { "No project found for id: $id" }
        throw ProjectNotFoundException(id)
    }

    fun createProject(projectRequest: ProjectDTO): ProjectDTO {
        logger.info { "Creating new project" }
        if (projectRequest.id == null) {
            if (projectRequest.endDate != null) {
                if (projectRequest.startDate >= projectRequest.endDate) {
                    val exception = UnableToCreateProjectException("Cannot create a project with end date preceding start date.")
                    logger.error(exception) { "Failed creating a new project" }
                    throw exception
                }
            }
            val project = Project(projectRequest)
            try {
                return ProjectDTO(projectRepository.save(project))
            } catch (e: DataIntegrityViolationException) {
                logger.error(e) { "Failed creating a new project" }
                throw UnableToCreateProjectException("Cannot create a project with non-existing relation")
            }
        } else {
            val exception = UnableToCreateProjectException("Cannot create a project with existing id")
            logger.error(exception) { "Failed creating a new project" }
            throw exception
        }
    }

    fun updateProjectForId(projectRequest: ProjectDTO): ProjectDTO {
        logger.info { "Updating project with id: ${projectRequest.id}" }
        if (projectRequest.id != null) {
            if (projectRequest.endDate != null) {
                if (projectRequest.startDate >= projectRequest.endDate) {
                    val exception = UnableToUpdateProjectException("Cannot update a project with end date preceding start date.")
                    logger.error(exception) { "Failed updating project" }
                    throw exception
                }
            }
            val project = Project(projectRequest)
            return ProjectDTO(projectRepository.save(project))
        } else {
            val exception = UnableToUpdateProjectException("Cannot update project, missing project id")
            logger.error(exception) { "Cannot update customer" }
            throw exception
        }
    }

    fun deleteProject(projectId: Long) {
        try {
            logger.info { "Deleting project with id: $projectId" }
            projectRepository.deleteById(projectId)
        } catch (e: EmptyResultDataAccessException) {
            logger.error(e) { "Cannot delete project" }
            throw UnableToDeleteProjectException(projectId)
        }
    }

    fun findAllProjects(): List<ProjectDTO> {
        val projects = projectRepository.findAll()
        return projects.map { ProjectDTO(it) }
    }
}

class UnableToCreateProjectException(message: String) : RuntimeException(message)
class UnableToUpdateProjectException(message: String) : RuntimeException(message)
class UnableToDeleteProjectException(id: Long) :
    RuntimeException("Cannot delete project, no project found for given id: $id")
class ProjectNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found for id: $id")
