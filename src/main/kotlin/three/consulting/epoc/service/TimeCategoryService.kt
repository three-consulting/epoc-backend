package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import three.consulting.epoc.dto.TimeCategoryDTO
import three.consulting.epoc.entity.TimeCategory
import three.consulting.epoc.repository.TimeCategoryRepository

private val logger = KotlinLogging.logger {}

@Service
class TimeCategoryService(private val timeCategoryRepository: TimeCategoryRepository) {

    fun findTimeCategoryForId(id: Long): TimeCategoryDTO? {
        logger.info { "Looking for time category with id: $id" }
        val timeCategory: TimeCategory? = timeCategoryRepository.findByIdOrNull(id)
        if (timeCategory != null) {
            return TimeCategoryDTO(timeCategory)
        }
        logger.info { "No time category found for the id: $id" }
        throw TimeCategoryNotFoundException(id)
    }

    fun createTimeCategory(timeCategoryRequest: TimeCategoryDTO): TimeCategoryDTO {
        logger.info("Creating new time category")
        if (timeCategoryRequest.id == null) {
            val timeCategory = TimeCategory(timeCategoryRequest)
            return TimeCategoryDTO(timeCategoryRepository.save(timeCategory))
        } else {
            val exception = UnableToCreateTimeCategoryException()
            logger.error(exception) { "Failed creating a new time category" }
            throw exception
        }
    }

    fun updateTimeCategoryForId(timeCategoryRequest: TimeCategoryDTO): TimeCategoryDTO {
        logger.info { "Updating time category with id: ${timeCategoryRequest.id}" }
        if (timeCategoryRequest.id != null) {
            val timeCategory = TimeCategory(timeCategoryRequest)
            return TimeCategoryDTO(timeCategoryRepository.save(timeCategory))
        } else {
            val exception = UnableToUpdateTimeCategoryException()
            logger.error(exception) { "Cannot update time category" }
            throw exception
        }
    }

    fun deleteTimeCategory(timeCategoryId: Long) {
        try {
            logger.info { "Deleting time category with id: $timeCategoryId" }
            timeCategoryRepository.deleteById(timeCategoryId)
        } catch (e: EmptyResultDataAccessException) {
            logger.error(e) { "Cannot delete time category" }
            throw UnableToDeleteTimeCategoryException(timeCategoryId)
        }
    }

    fun findAllTimeCategories(): List<TimeCategoryDTO> {
        val employees = timeCategoryRepository.findAll()
        return employees.map { TimeCategoryDTO(it) }
    }
}

class UnableToCreateTimeCategoryException : RuntimeException("Cannot create a time category with existing id")
class UnableToUpdateTimeCategoryException : RuntimeException("Cannot update time category, missing time category id")
class UnableToDeleteTimeCategoryException(id: Long) :
    RuntimeException("Cannot delete time category, no time category found for the given id: $id")
class TimeCategoryNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Time category not found for id: $id")
