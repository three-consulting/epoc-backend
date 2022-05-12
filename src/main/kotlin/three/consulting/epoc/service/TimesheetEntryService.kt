package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import three.consulting.epoc.dto.TimesheetEntryDTO
import three.consulting.epoc.entity.TimesheetEntry
import three.consulting.epoc.repository.TimesheetEntryRepository
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Service
class TimesheetEntryService(private val timesheetEntryRepository: TimesheetEntryRepository) {

    fun findTimesheetEntries(timesheetId: Long?, email: String?, startDate: LocalDate?, endDate: LocalDate?): List<TimesheetEntryDTO> {
        logger.info { "Looking for timesheetEntries with timesheetId: $timesheetId, email: $email, startDate: $startDate and endDate: $endDate" }
        return when {
            timesheetId != null -> timesheetEntryRepository.findAllByTimesheetId(timesheetId).map { TimesheetEntryDTO(it) }
            email != null && startDate != null && endDate != null -> timesheetEntryRepository.findAllByEmployeeEmailAndDates(email, startDate, endDate).map { TimesheetEntryDTO(it) }
            else -> throw UnableToGetTimesheetEntriesException()
        }
    }

    fun findTimesheetEntryForId(id: Long): TimesheetEntryDTO? {
        logger.info { "Looking for timesheetEntry with id: $id" }
        val timesheetEntry: TimesheetEntry? = timesheetEntryRepository.findByIdOrNull(id)
        if (timesheetEntry != null)
            return TimesheetEntryDTO(timesheetEntry)
        logger.info { "No timesheetEntry found for id: $id" }
        throw TimeSheetEntryNotFoundException(id)
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

class UnableToGetTimesheetEntriesException : RuntimeException("Cannot get timesheetEntries, invalid request parameters")

class UnableToCreateTimesheetEntryException(message: String) : RuntimeException(message)
class UnableToUpdateTimesheetEntryException : RuntimeException("Cannot update timesheetEntry, missing timesheetEntry id")
class UnableToDeleteTimesheetEntryException(id: Long) :
    RuntimeException("Cannot delete timesheetEntry, no timesheetEntry found for given id: $id")
class TimeSheetEntryNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Timesheet entry not found for id: $id")
