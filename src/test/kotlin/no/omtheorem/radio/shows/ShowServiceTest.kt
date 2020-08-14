package no.omtheorem.radio.shows

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

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

}