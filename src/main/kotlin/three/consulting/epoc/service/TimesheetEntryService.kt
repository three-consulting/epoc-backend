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
    fun findTimesheetEntries(
        timesheetId: Long?,
        email: String?,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): List<TimesheetEntryDTO> {
        logger.info { "Looking for timesheetEntries with timesheetId: $timesheetId, email: $email, startDate: $startDate and endDate: $endDate" }
        return when {
            timesheetId != null -> timesheetEntryRepository.findAllByTimesheetId(timesheetId).map { TimesheetEntryDTO(it) }
            email != null && startDate != null && endDate != null -> timesheetEntryRepository.findAllByEmployeeEmailAndDates(email, startDate, endDate).map { TimesheetEntryDTO(it) }
            email == null && startDate != null && endDate != null && startDate.isBefore(endDate) -> timesheetEntryRepository.findAllByDates(startDate, endDate).map { TimesheetEntryDTO(it) }

            else -> throw UnableToGetTimesheetEntriesException()
        }
    }

    fun findTimesheetEntryForId(id: Long): TimesheetEntryDTO? {
        logger.info { "Looking for timesheetEntry with id: $id" }
        val timesheetEntry: TimesheetEntry? = timesheetEntryRepository.findByIdOrNull(id)
        timesheetEntry?.let {
            return TimesheetEntryDTO(it)
        }
        logger.info { "No timesheetEntry found for id: $id" }
        throw TimeSheetEntryNotFoundException(id)
    }

    fun findEmployeeFlexByEmail(email: String): Float =
        try {
            logger.info { "Looking for employee flex with email: $email" }
            timesheetEntryRepository.sumEmployeeFLexByEmail(email)
        } catch (exc: EmptyResultDataAccessException) {
            logger.error { "No flex found for email: $email" }
            throw FlexNotFoundException(email)
        }

    fun exportToCsv(
        startDate: LocalDate,
        endDate: LocalDate,
        email: String? = null,
        projectId: Long? = null,
        customerId: Long? = null,
        taskId: Long? = null
    ): String {
        val entries =
            timesheetEntryRepository.findAllByParams(
                startDate,
                endDate,
                email,
                projectId,
                customerId,
                taskId,
            )

        var columnNames = "hours;task;project;date;email\n"
        val sortedEntries = entries.sortedBy { it.date }
        val rows = sortedEntries.joinToString("") { entry -> "${entry.quantity};${entry.task.name};${entry.timesheet.project.name};${entry.date};${entry.timesheet.employee.email}\n" }
        return columnNames + rows
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

    fun createTimesheetEntries(timesheetEntries: List<TimesheetEntryDTO>): List<TimesheetEntryDTO> {
        return timesheetEntries.map { createTimesheetEntry(it) }
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

    fun updateTimesheetEntriesForId(timesheetEntries: List<TimesheetEntryDTO>): List<TimesheetEntryDTO> {
        return timesheetEntries.map { updateTimesheetEntryForId(it) }
    }

    fun deleteTimesheetEntryForId(timesheetEntryId: Long) {
        logger.info { "Deleting timesheetEntry with id: $timesheetEntryId" }
        timesheetEntryRepository.deleteById(timesheetEntryId)
    }

    fun deleteTimesheetEntriesForId(timesheetIds: List<Long>) {
        timesheetIds.map { deleteTimesheetEntryForId(it) }
    }

    fun hasValidEmails(
        timesheetEntries: List<TimesheetEntryDTO>,
        email: String
    ): Boolean {
        return timesheetEntries.all { it.timesheet.employee.email == email }
    }
}

class UnableToGetTimesheetEntriesException : RuntimeException("Cannot get timesheetEntries, invalid request parameters")

class UnableToCreateTimesheetEntryException(message: String) : RuntimeException(message)

class UnableToUpdateTimesheetEntryException : RuntimeException("Cannot update timesheetEntry, missing timesheetEntry id")

class TimeSheetEntryNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Timesheet entry not found for id: $id")

class FlexNotFoundException(email: String) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Flex not found for email: $email")
