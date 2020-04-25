package no.omtheorem.radio.shows

import no.omtheorem.radio.tracks.TrackEntity
import no.omtheorem.radio.tracks.TrackRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class ShowsRepositoryTest {

    @Autowired
    private lateinit var showRepository: ShowRepository

    @Autowired
    private lateinit var trackRepository: TrackRepository

    @Test
    fun `save a show with tracks saves them to the tracks repository`() {
        val initialShowWithoutTracks = ShowEntity()

        val showWithTracks = showRepository.save(initialShowWithoutTracks)
        val trackA = TrackEntity("Artist A", "Track A", 1)
        val trackB = TrackEntity("Artist B", "Track B", 2)
        val tracks = showWithTracks.tracks.toMutableList()
        tracks.addAll(listOf(trackA, trackB))
        showWithTracks.tracks = tracks

        showRepository.save(showWithTracks)

        assertThat(showRepository.findAll()).contains(showWithTracks)
        assertThat(trackRepository.findAll()).contains(trackA, trackB)
    }

}