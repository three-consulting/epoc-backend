package three.consulting.epoc.controller

import org.springframework.web.bind.annotation.*
import three.consulting.epoc.dto.ProjectDTO
import three.consulting.epoc.service.ProjectService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/project"])
class ProjectController(private val projectService: ProjectService) {

    @GetMapping(value = ["/{projectId}"])
    fun getCustomerForId(@PathVariable projectId: Long) = projectService.findProjectForId(projectId)

    @PostMapping
    fun createProject(@Valid @RequestBody project: ProjectDTO) = projectService.createProject(project)

    @PutMapping
    fun updateProjectForId(@Valid @RequestBody project: ProjectDTO) = projectService.updateProjectForId(project)

    @DeleteMapping(value = ["/{projectId}"])
    fun deleteCustomerForId(@PathVariable projectId: Long) = projectService.deleteProject(projectId)
}
