package no.omtheorem.radio.shows

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import no.omtheorem.radio.tracks.TrackEntity
import no.omtheorem.radio.tracks.TrackForm
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.*

@WebMvcTest
internal class ShowsControllerTest(@Autowired var mvc:MockMvc) {

    @MockkBean(relaxUnitFun = true)
    private lateinit var showRepository: ShowRepository

    @Test
    fun `showCreateShowForm renders form`() {
        this.mvc.perform(get("/shows/create"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/create"))
    }

    @Test
    fun `createShow saves the show to the repository and redirects to root`() {
        val show = ShowEntity("Show Name", LocalDate.of(2020, 4, 12))

        every { showRepository.save(show) } returns show

        this.mvc.perform(post("/shows/create")
                .param("name", show.name)
                .param("date", show.date.toString())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/"))

        verify (exactly = 1) { showRepository.save(show) }
    }

    @Test
    fun `createShow empty name is not valid and returns the same view`() {
        val showForm = ShowForm("", "2020-04-28")

        this.mvc.perform(post("/shows/create")
                .param("name", showForm.name)
                .param("date", showForm.date)
        )
                .andExpect(status().isOk)
                .andExpect(view().name("shows/create"))
                .andExpect(model().attribute("show", showForm))
    }

    @Test
    fun `createShow empty date is not valid and returns the same view`() {
        val showForm = ShowForm("A name", "")

        this.mvc.perform(post("/shows/create")
                .param("name", showForm.name)
                .param("date", showForm.date)
        )
                .andExpect(status().isOk)
                .andExpect(view().name("shows/create"))
                .andExpect(model().attribute("show", showForm))
    }

    @Test
    fun `listShows renders all shows ordered by descending id`() {
        val allShows = listOf(
                ShowEntity("First", LocalDate.of(2020, 3, 15), 1),
                ShowEntity("Second", LocalDate.of(2020, 4, 12), 2),
                ShowEntity("Third", LocalDate.of(2020, 5, 17), 3)
        )

        every { showRepository.findAll() } returns allShows

        this.mvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(view().name("index"))
                .andExpect(model().attribute("shows", allShows.sortedByDescending { it.id }))
    }

    @Test
    fun `deleteShow deletes a show by its id`() {
        this.mvc.perform(get("/shows/delete/2"))
                .andExpect(status().is3xxRedirection)

        verify { showRepository.deleteById(2) }
    }

    @Test
    fun `showUpdateShowForm renders form with correct values`() {
        val show = ShowEntity("Edit me!", LocalDate.of(2020, 4, 19), 1)

        every { showRepository.findById(1) } returns Optional.of(show)

        this.mvc.perform(get("/shows/update/1"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/update"))
                .andExpect(model().attribute("show", ShowForm(show.name, show.date.toString(), 1)))
    }

    @Test
    fun `updateShow updates the show and redirects to root`() {
        val showEntity = ShowEntity("New name", LocalDate.of(2020, 4, 22), 1)

        every { showRepository.save(showEntity) } returns showEntity

        this.mvc.perform(post("/shows/update/1")
                .param("name", showEntity.name)
                .param("date", showEntity.date.toString())
                .param("id", showEntity.id.toString())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/"))

        verify { showRepository.save(showEntity) }
    }

    @Test
    fun `getDetails displays show details on the details page`() {
        val show = ShowEntity("Show me!", LocalDate.of(2020, 5, 14), 1)

        every { showRepository.findById(1) } returns Optional.of(show)

        this.mvc.perform(get("/shows/1"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/show"))
                .andExpect(model().attribute("show", ShowForm(show.name, show.date.toString(), show.id)))
    }

    @Test
    fun `showCreateTrackForm renders form`() {
        this.mvc.perform(get("/shows/1/tracks/create"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/tracks/create"))
                .andExpect(model().attribute("track", TrackForm("", "")))
                .andExpect(model().attribute("showId", 1L))
    }

    @Test
    fun `createTrack adds a track to a show`() {
        val alreadyExistingTrack = TrackEntity("Existing Artist", "Track!!")
        val addedTrack = TrackEntity("Added Artist", "Track number two")
        val show = ShowEntity(id = 1, tracks = listOf(alreadyExistingTrack))

        every { showRepository.findById(1) } returns Optional.of(show)

        val updatedShow = show.copy()
        updatedShow.tracks = listOf(alreadyExistingTrack, addedTrack)

        every { showRepository.save(updatedShow) } returns updatedShow

        this.mvc.perform(post("/shows/1/tracks")
                .param("artist", addedTrack.artist)
                .param("name", addedTrack.name)
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/shows/1"))

        verify { showRepository.save(updatedShow) }
    }

}