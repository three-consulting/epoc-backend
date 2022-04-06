package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import three.consulting.epoc.dto.TimesheetEntryDTO
import three.consulting.epoc.entity.TimesheetEntry
import three.consulting.epoc.repository.TimesheetEntryRepository

private val logger = KotlinLogging.logger {}

@Service
class TimesheetEntryService(private val timesheetEntryRepository: TimesheetEntryRepository) {

    fun findTimesheetEntryForId(id: Long): TimesheetEntryDTO? {
        logger.info { "Looking for timesheetEntry with id: $id" }
        val timesheetEntry: TimesheetEntry? = timesheetEntryRepository.findByIdOrNull(id)
        if (timesheetEntry != null)
            return TimesheetEntryDTO(timesheetEntry)
        logger.info { "No timesheetEntry found for id: $id" }
        return null
    }

    fun findTimesheetEntriesForTimesheetId(timesheetId: Long): List<TimesheetEntryDTO> {
        logger.info { "Looking for timesheetEntries with timesheetId: $timesheetId" }

        return timesheetEntryRepository.findAllByTimesheetId(timesheetId).map { TimesheetEntryDTO(it) }
    }

    fun createTimesheetEntry(timesheetEntryRequest: TimesheetEntryDTO): TimesheetEntryDTO {
        logger.info { "Creating new timesheetEntry" }
        if (timesheetEntryRequest.id == null) {
            val timesheetEntry = TimesheetEntry(timesheetEntryRequest)
            try {
                return TimesheetEntryDTO(timesheetEntryRepository.save(timesheetEntry))
            } catch (e: DataIntegrityViolationException) {
                logger.error(e) { "Failed creating a new timesheetEntry" }
                throw UnableToCreateTimesheetEntryException("Cannot create a timesheetEntry with non-existing relation")
            }
        } else {
            val exception = UnableToCreateTimesheetEntryException("Cannot create a timesheetEntry with existing id")
            logger.error(exception) { "Failed creating a new timesheetEntry" }
            throw exception
        }
    }

    fun updateTimesheetEntryForId(timesheetEntryRequest: TimesheetEntryDTO): TimesheetEntryDTO {
        logger.info { "Updating timesheetEntry with id: ${timesheetEntryRequest.id}" }
        if (timesheetEntryRequest.id != null) {
            val timesheetEntry = TimesheetEntry(timesheetEntryRequest)
            return TimesheetEntryDTO(timesheetEntryRepository.save(timesheetEntry))
        } else {
            val exception = UnableToUpdateTimesheetEntryException()
            logger.error(exception) { "Cannot update customer" }
            throw exception
        }
    }

    fun deleteTimesheetEntry(timesheetEntryId: Long) {
        try {
            logger.info { "Deleting timesheetEntry with id: $timesheetEntryId" }
            timesheetEntryRepository.deleteById(timesheetEntryId)
        } catch (e: EmptyResultDataAccessException) {
            logger.error(e) { "Cannot delete timesheetEntry" }
            throw UnableToDeleteTimesheetEntryException(timesheetEntryId)
        }
    }
}

class UnableToCreateTimesheetEntryException(message: String) : RuntimeException(message)
class UnableToUpdateTimesheetEntryException : RuntimeException("Cannot update timesheetEntry, missing timesheetEntry id")
class UnableToDeleteTimesheetEntryException(id: Long) :
    RuntimeException("Cannot delete timesheetEntry, no timesheetEntry found for given id: $id")
