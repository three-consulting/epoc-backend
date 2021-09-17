package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.entity.Project
import three.consulting.epoc.repository.ProjectRepository

private val logger = KotlinLogging.logger {}

@Service
class ProjectService(private val projectRepository: ProjectRepository) {

    fun findProjectForId(id: Long): ProjectDTO? {
        logger.info { "Looking for project with id: $id" }
        val project: Project? = projectRepository.findByIdOrNull(id)
        if (project != null)
            return ProjectDTO(project)
        logger.info { "No project found for id: $id" }
        return null
    }

    fun createProject(projectRequest: ProjectDTO): ProjectDTO {
        logger.info { "Creating new project" }
        if (projectRequest.id == null) {
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
            val project = Project(projectRequest)
            return ProjectDTO(projectRepository.save(project))
        } else {
            val exception = UnableToUpdateProjectException()
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
}

class UnableToCreateProjectException(message: String) : RuntimeException(message)
class UnableToUpdateProjectException : RuntimeException("Cannot update project, missing project id")
class UnableToDeleteProjectException(id: Long) :
    RuntimeException("Cannot delete project, no project found for given id: $id")
