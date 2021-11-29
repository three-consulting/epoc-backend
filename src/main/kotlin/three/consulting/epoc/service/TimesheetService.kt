package three.consulting.epoc.service

import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import three.consulting.epoc.dto.TimesheetDTO
import three.consulting.epoc.entity.Timesheet
import three.consulting.epoc.repository.TimesheetRepository

private val logger = KotlinLogging.logger {}

@Service
class TimesheetService(private val timesheetRepository: TimesheetRepository) {

    fun findTimesheetForProjectId(id: Long): List<TimesheetDTO>? {
        logger.info { "Looking for timesheet with project_id: $id" }
        val timesheet: List<Timesheet> = timesheetRepository.findAllByProjectId(id)
        if (timesheet.isNotEmpty())
            return timesheet.map { TimesheetDTO(it) }
        logger.info { "No timesheet found for id: $id" }
        return null
    }

    fun findTimesheetForId(id: Long): TimesheetDTO? {
        logger.info { "Looking for timesheet with id: $id" }
        val timesheet: Timesheet? = timesheetRepository.findByIdOrNull(id)
        if (timesheet != null)
            return TimesheetDTO(timesheet)
        logger.info { "No timesheet found for id: $id" }
        return null
    }

    fun createTimesheet(timesheetRequest: TimesheetDTO): TimesheetDTO {
        logger.info { "Creating new timesheet" }
        if (timesheetRequest.id == null) {
            val timesheet = Timesheet(timesheetRequest)
            try {
                return TimesheetDTO(timesheetRepository.save(timesheet))
            } catch (e: DataIntegrityViolationException) {
                logger.error(e) { "Failed creating a new timesheet" }
                throw UnableToCreateTimesheetException("Cannot create a timesheet with non-existing relation")
            }
        } else {
            val exception = UnableToCreateTimesheetException("Cannot create a timesheet with existing id")
            logger.error(exception) { "Failed creating a new timesheet" }
            throw exception
        }
    }

    fun updateTimesheetForId(timesheetRequest: TimesheetDTO): TimesheetDTO {
        logger.info { "Updating timesheet with id: ${timesheetRequest.id}" }
        if (timesheetRequest.id != null) {
            val timesheet = Timesheet(timesheetRequest)
            return TimesheetDTO(timesheetRepository.save(timesheet))
        } else {
            val exception = UnableToUpdateTimesheetException()
            logger.error(exception) { "Cannot update customer" }
            throw exception
        }
    }

    fun deleteTimesheet(timesheetId: Long) {
        try {
            logger.info { "Deleting timesheet with id: $timesheetId" }
            timesheetRepository.deleteById(timesheetId)
        } catch (e: EmptyResultDataAccessException) {
            logger.error(e) { "Cannot delete timesheet" }
            throw UnableToDeleteTimesheetException(timesheetId)
        }
    }
}

class UnableToCreateTimesheetException(message: String) : RuntimeException(message)
class UnableToUpdateTimesheetException : RuntimeException("Cannot update timesheet, missing timesheet id")
class UnableToDeleteTimesheetException(id: Long) :
    RuntimeException("Cannot delete timesheet, no timesheet found for given id: $id")
