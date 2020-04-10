package no.omtheorem.radio

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
internal class ShowsControllerTest(@Autowired var mvc:MockMvc) {

    @Test
    fun `Add new show page exists`() {
        this.mvc.perform(get("/shows/add"))
                .andExpect(status().isOk)
    }

}