package no.omtheorem.radio

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@WebMvcTest
internal class ShowsControllerTest(@Autowired var mvc:MockMvc) {

    @Test
    fun `Add show renders form`() {
        this.mvc.perform(get("/shows/add"))
                .andExpect(status().isOk)
                .andExpect(view().name("shows/add"))
    }

}