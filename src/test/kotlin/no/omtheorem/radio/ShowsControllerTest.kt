package no.omtheorem.radio

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.*

@WebMvcTest
internal class ShowsControllerTest(@Autowired var mvc:MockMvc) {

    @MockkBean
    private lateinit var showRepository: ShowRepository

    @Test
    fun `showAddShowForm renders form`() {
        this.mvc.perform(get("/shows/add"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/add"))
    }

    @Test
    fun `addShow redirects to root`() {
        every { showRepository.save(ShowEntity()) } returns ShowEntity()

        this.mvc.perform(post("/shows"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/"))
    }

    @Test
    fun `addShow saves the show to the repository`() {
        val show = ShowEntity("Show Name","2020-04-12")

        every { showRepository.save(show) } returns ShowEntity(show.name, show.date, 1)

        this.mvc.perform(post("/shows")
                .param("name", show.name)
                .param("date", show.date))

        verify (exactly = 1) { showRepository.save(show) }
    }

    @Test
    fun `listShows renders all shows`() {
        val allShows = listOf(
                ShowEntity("First", "2020-03-15", 1),
                ShowEntity("Second", "2020-04-12", 2)
        )

        every { showRepository.findAll() } returns allShows

        this.mvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(view().name("index"))
                .andExpect(model().attribute("shows", allShows))
    }

}