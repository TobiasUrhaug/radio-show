package no.omtheorem.radio.shows

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import no.omtheorem.radio.tracks.TrackEntity
import no.omtheorem.radio.tracks.TrackForm
import no.omtheorem.radio.tracks.TrackRepository
import no.omtheorem.radio.tracks.TracklistForm
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.*

@WebMvcTest
@WithMockUser("user")
internal class ShowsControllerTest(@Autowired var mvc:MockMvc) {

    @MockkBean(relaxUnitFun = true)
    private lateinit var showRepository: ShowRepository

    @MockkBean(relaxUnitFun = true)
    private lateinit var showService: ShowService

    @MockkBean(relaxUnitFun = true)
    private lateinit var trackRepository: TrackRepository

    @Test
    fun `showCreateShowForm renders form`() {
        this.mvc.perform(get("/shows/create"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/create-show"))
    }

    @Test
    fun `createShow saves the show to the repository`() {
        val show = ShowForm("Show Name", "2020-04-13")

        every { showService.createShow(show) } just runs

        this.mvc.perform(post("/shows/create")
                .param("name", show.name)
                .param("date", show.date)
        )

        verify (exactly = 1) { showService.createShow(show) }
    }

    @Test
    fun `createShow redirects to root`() {
        val show = ShowForm("Show Name", "2020-04-17")

        every { showService.createShow(show) } just runs

        this.mvc.perform(post("/shows/create")
                .param("name", show.name)
                .param("date", show.date)
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/"))
    }

    @Test
    fun `createShow empty name is not valid and returns the same view`() {
        val showForm = ShowForm("", "2020-04-28")

        this.mvc.perform(post("/shows/create")
                .param("name", showForm.name)
                .param("date", showForm.date)
        )
                .andExpect(status().isOk)
                .andExpect(view().name("shows/create-show"))
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
                .andExpect(view().name("shows/create-show"))
                .andExpect(model().attribute("show", showForm))
    }

    @Test
    fun `listShows renders all shows`() {
        val allShows = listOf(
                ShowForm("First", "2020-03-15", id = 1L),
                ShowForm("Second", "2020-04-16", id = 2L),
                ShowForm("Third", "2020-05-17", id = 3L)
        )

        every { showService.listShows() } returns allShows

        this.mvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(view().name("index"))
                .andExpect(model().attribute("shows", allShows))
    }

    @Test
    fun `deleteShow deletes a show by its id`() {
        this.mvc.perform(get("/shows/delete/2"))
                .andExpect(status().is3xxRedirection)

        verify { showService.deleteShow(2L) }
    }

    @Test
    fun `showUpdateShowForm renders form with correct values`() {
        val show = ShowForm("Edit me!", "2020-04-19", id = 1)

        every { showService.findById(1) } returns show

        this.mvc.perform(get("/shows/1/update"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/update"))
                .andExpect(model().attribute("show", ShowForm(show.name, show.date.toString())))
                .andExpect(model().attribute("showId", 1L))
    }

    @Test
    fun `updateShowDetails updates the show and redirects to the shows details page`() {
        val existingShow = ShowForm("name", "2020-04-22", emptyList(), 1)
        every { showService.findById(1) } returns existingShow

        val updatedShow = ShowForm("updated name", "2020-04-24", emptyList(), existingShow.id)
        every { showService.updateShowDetails(updatedShow) } returns updatedShow

        this.mvc.perform(post("/shows/1/update")
                .param("name", updatedShow.name)
                .param("date", updatedShow.date)
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/shows/1"))

        verify { showService.updateShowDetails(updatedShow) }
    }

    @Test
    fun `getDetails displays show details on the details page`() {
        val show = ShowForm("Show me!", "2020-05-14", emptyList(),1)

        every { showService.findById(show.id) } returns show

        this.mvc.perform(get("/shows/1"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/show"))
                .andExpect(model().attribute("show", show))
                .andExpect(model().attribute("showId", show.id))
    }

    @Test
    fun `getDetails returns 404 when show is not found`() {
        every { showService.findById(1) } throws ShowNotFoundException("")

        this.mvc.perform(get("/shows/1"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `showManageTracklistForm renders form with already existing tracks`() {
        val show = ShowEntity(id = 1, tracks = emptyList())
        every { showRepository.findById(1) } returns Optional.of(show)

        this.mvc.perform(get("/shows/1/tracks/create"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/tracklist/manage-tracklist"))
                .andExpect(model().attribute("tracklist", TracklistForm(emptyList())))
                .andExpect(model().attribute("showId", 1L))

        val trackA = TrackEntity(
                "Artist A",
                "Track A",
                url = "url A",
                remix = "remix A",
                label = "label A"
        )
        val trackB = TrackEntity(
                "Artist B",
                "Track B",
                url = "url B",
                remix = "remix B",
                label = "label B"
        )
        show.tracks = listOf(trackA, trackB)

        val tracklist = TracklistForm(listOf(
            TrackForm(trackA.artist, trackA.name, trackA.url, trackA.remix, trackA.label),
            TrackForm(trackB.artist, trackB.name, trackB.url, trackB.remix, trackB.label)
        ))

        this.mvc.perform(get("/shows/1/tracks/create"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/tracklist/manage-tracklist"))
                .andExpect(model().attribute("tracklist", tracklist))
                .andExpect(model().attribute("showId", 1L))
    }

    @Test
    fun `createTracklist adds a tracklist to a show`() {
        val show = ShowEntity(id = 1, tracks = listOf())

        every { showRepository.findById(1) } returns Optional.of(show)

        val updatedShow = show.copy()
        val addedTrackA = TrackEntity(
                "Added Artist A",
                "Track number two",
                url = "url A",
                remix = "Rmx A",
                label = "Label A"
        )
        val addedTrackB = TrackEntity(
                "Added Artist B",
                "Track number three",
                url = "url B",
                remix = "Rmx B",
                label = "Label B"
        )
        updatedShow.tracks = listOf(addedTrackA, addedTrackB)

        every { showRepository.save(updatedShow) } returns updatedShow

        this.mvc.perform(post("/shows/1/tracks")
                .param("tracks[0].artist", addedTrackA.artist)
                .param("tracks[0].title", addedTrackA.name)
                .param("tracks[0].url", addedTrackA.url)
                .param("tracks[0].remix", addedTrackA.remix)
                .param("tracks[0].label", addedTrackA.label)
                .param("tracks[1].artist", addedTrackB.artist)
                .param("tracks[1].title", addedTrackB.name)
                .param("tracks[1].url", addedTrackB.url)
                .param("tracks[1].remix", addedTrackB.remix)
                .param("tracks[1].label", addedTrackB.label)
        )

        verify { showRepository.save(updatedShow) }
    }

    @Test
    fun `createTracklist omits empty tracks`() {
        val show = ShowEntity(id = 1, tracks = emptyList())

        every { showRepository.findById(1) } returns Optional.of(show)

        val updatedShow = show.copy()
        val newTrack = TrackEntity("Added Artist A", "Track number two", url = "url A")
        updatedShow.tracks = listOf(newTrack)

        every { showRepository.save(updatedShow) } returns updatedShow

        this.mvc.perform(post("/shows/1/tracks")
                .param("tracks[0].artist", "")
                .param("tracks[0].title", "")
                .param("tracks[0].url", "")
                .param("tracks[1].artist", newTrack.artist)
                .param("tracks[1].title", newTrack.name)
                .param("tracks[1].url", newTrack.url)
        )

        verify { showRepository.save(updatedShow) }
    }

    @Test
    fun `createTracklist redirects to the shows url`() {
        val show = ShowEntity(id = 1, tracks = listOf())

        every { showRepository.findById(1) } returns Optional.of(show)
        every { showRepository.save(show) } returns show

        this.mvc.perform(post("/shows/1/tracks"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/shows/1"))
    }

}