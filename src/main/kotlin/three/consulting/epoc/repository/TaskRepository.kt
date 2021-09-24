package three.consulting.epoc.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import three.consulting.epoc.entity.Task

@Repository
interface TaskRepository : JpaRepository<Task, Long>
