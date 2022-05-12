package three.consulting.epoc.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import three.consulting.epoc.IntegrationTest
import three.consulting.epoc.dto.TimeCategoryDTO
import three.consulting.epoc.repository.TimeCategoryRepository

@ContextConfiguration(classes = [TimeCategoryService::class])
class TimeCategoryServiceIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var timeCategoryService: TimeCategoryService

    @Autowired
    private lateinit var timeCategoryRepository: TimeCategoryRepository

    @Test
    fun `searching a timeCategory for id returns a timeCategory`() {
        val timeCategory: TimeCategoryDTO = timeCategoryService.findTimeCategoryForId(1L)!!

        assertThat(timeCategory.name).isEqualTo("Test work")
        assertThat(timeCategory.description).isEqualTo("Testing time category")
    }

    @Test
    fun `searching a timeCategory for an invalid id returns null`() {
        assertThatThrownBy { timeCategoryService.findTimeCategoryForId(1000000L) }
            .isInstanceOf(TimeCategoryNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Time category not found for id: 1000000\"")
    }

    @Test
    fun `added timeCategory is found from database`() {
        val timeCategory = TimeCategoryDTO(
            name = "New time",
            description = "new time category"
        )
        val addedTimeCategory: TimeCategoryDTO = timeCategoryService.createTimeCategory(timeCategory)
        assertThat(addedTimeCategory.name).isEqualTo(timeCategory.name)
        assertThat(addedTimeCategory.description).isEqualTo(timeCategory.description)
    }

    @Test
    fun `adding timeCategory with id fails`() {
        val invalidTimeCategory = TimeCategoryDTO(1, "Testi Oy", "Innovating new innovation!")
        assertThatThrownBy { timeCategoryService.createTimeCategory(invalidTimeCategory) }
            .isInstanceOf(UnableToCreateTimeCategoryException::class.java)
            .hasMessage("Cannot create a time category with existing id")
    }

    @Test
    fun `update timeCategory with id changes updated time`() {
        val existingTimeCategory = timeCategoryService.findTimeCategoryForId(1L)
        if (existingTimeCategory != null) {
            val updatedTimeCategory = timeCategoryService.updateTimeCategoryForId(existingTimeCategory)
            assertThat(updatedTimeCategory.updated).isNotEqualTo(existingTimeCategory.updated)
        }
    }

    @Test
    fun `update timeCategory without id raises error`() {
        val invalidTimeCategory = TimeCategoryDTO(name = "Failure Ltd")
        assertThatThrownBy { timeCategoryService.updateTimeCategoryForId(invalidTimeCategory) }
            .isInstanceOf(UnableToUpdateTimeCategoryException::class.java)
            .hasMessage("Cannot update time category, missing time category id")
    }

    @Test
    fun `delete timeCategory removes timeCategory from the database`() {
        assertThat(timeCategoryRepository.findByIdOrNull(2L)).isNotNull
        timeCategoryService.deleteTimeCategory(2L)
        assertThat(timeCategoryRepository.findByIdOrNull(2L)).isNull()
    }

    @Test
    fun `delete timeCategory with non-existing id raises error`() {
        assertThatThrownBy { timeCategoryService.deleteTimeCategory(1000L) }
            .isInstanceOf(UnableToDeleteTimeCategoryException::class.java)
            .hasMessage("Cannot delete time category, no time category found for the given id: 1000")
    }

    @Test
    fun `get all timeCategories`() {
        val timeCategories = timeCategoryService.findAllTimeCategories()
        assertThat(timeCategories.map { it.name }).containsExactlyElementsOf(listOf("Test work", "Actual work"))
    }
}
