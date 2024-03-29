package three.consulting.epoc.controller

import jakarta.validation.Valid
import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.access.prepost.PostFilter
import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.service.ProjectService

@RestController
@RequestMapping(path = ["/project"])
class ProjectController(private val projectService: ProjectService) {
    @GetMapping(value = ["/{projectId}"], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getProjectForId(
        @PathVariable projectId: Long
    ) = projectService.findProjectForId(projectId)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createProject(
        @Valid @RequestBody
        project: ProjectDTO
    ) = projectService.createProject(project)

    @PutMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateProjectForId(
        @Valid @RequestBody
        project: ProjectDTO
    ) = projectService.updateProjectForId(project)

    @DeleteMapping(value = ["/{projectId}"], consumes = [ALL_VALUE])
    fun deleteProjectForId(
        @PathVariable projectId: Long
    ) = projectService.deleteProject(projectId)

    @PostFilter("hasAuthority('ADMIN') or filterObject.status.name() == 'ACTIVE'")
    @GetMapping(consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun getAllProjects() = projectService.findAllProjects()
}
