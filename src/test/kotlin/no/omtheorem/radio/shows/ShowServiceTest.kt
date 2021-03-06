package no.omtheorem.radio.shows

import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import no.omtheorem.radio.tracks.TrackEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.util.*

internal class ShowServiceTest {

    @RelaxedMockK
    private lateinit var showRepository: ShowRepository

    private lateinit var showService: ShowService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        showService = ShowService(showRepository)
    }

    @Test
    fun `createShow saves a show to the repository`() {
        val name: String = "Show name"
        val date: String = "2020-04-15"
        val showForm = ShowForm(name, date)
        val showEntity = ShowEntity(name, LocalDate.of(2020, 4,15))

        every { showRepository.save(showEntity) } returns showEntity

        showService.createShow(showForm)

        verify (exactly = 1) { showRepository.save(showEntity) }
    }

    @Test
    fun `listShows returns all shows in descending id order`() {
        val allShowEntities = listOf(
                ShowEntity("First", LocalDate.of(2020, 3,15), 1L),
                ShowEntity("Second", LocalDate.of(2020, 4,16), 2L),
                ShowEntity("Third", LocalDate.of(2020, 5,17), 3L)
        )
        every { showRepository.findAll() } returns allShowEntities

        val expectedShows = listOf(
                ShowForm("Third","2020-05-17", id = 3L),
                ShowForm("Second", "2020-04-16", id = 2L),
                ShowForm("First", "2020-03-15", id = 1L)
        )

        assertEquals(expectedShows, showService.listShows())
    }

    @Test
    fun `deleteShow deletes a show by id`() {
        val showEntity = ShowEntity("name", LocalDate.of(2020, 4,15), id = 2L)
        every { showRepository.deleteById(showEntity.id) } just runs
        showService.deleteShow(showEntity.id);
        verify (exactly = 1) { showRepository.deleteById(showEntity.id) }
    }

    @Test
    fun `findById finds a show by its id`() {
        val showEntity = ShowEntity("name", LocalDate.of(2020, 4,15), id = 2L)
        every { showRepository.findById(showEntity.id) } returns Optional.of(showEntity)
        val returnedShow = showService.findById(showEntity.id)
        assertEquals(ShowForm(showEntity), returnedShow)
    }

    @Test
    fun `findById throws ShowNotFoundException when show id is not found`() {
        every { showRepository.findById(123) } returns Optional.empty()
        assertThrows<ShowNotFoundException> { showService.findById(123)  }
    }

    @Test
    fun `updateShowDetails updates details on a show`() {
        val existingShowEntity = ShowEntity("Name", LocalDate.of(2020, 3, 29),1)
        val updatedShowEntity = ShowEntity("updated", LocalDate.of(2020, 4, 18),1)
        val updatedShow = ShowForm(updatedShowEntity)

        every { showRepository.findById(1) } returns Optional.of(existingShowEntity)
        every { showRepository.save(updatedShowEntity) } returns updatedShowEntity

        showService.updateShowDetails(updatedShow)

        verify { showRepository.save(updatedShowEntity) }
    }


    @Test
    fun `updateShowDetails preserves the exising shows tracks`() {
        val track = TrackEntity("artist", "title")
        val existingShowEntity =
                ShowEntity("Name", LocalDate.of(2020, 3, 29),1, listOf(track))
        val updatedShowEntity =
                ShowEntity("updated", LocalDate.of(2020, 4, 18),1, listOf(track))
        val updatedShow = ShowForm(updatedShowEntity)

        every { showRepository.findById(1) } returns Optional.of(existingShowEntity)
        every { showRepository.save(updatedShowEntity) } returns updatedShowEntity

        showService.updateShowDetails(updatedShow)

        verify { showRepository.save(updatedShowEntity) }
    }

}