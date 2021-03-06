package no.omtheorem.radio.shows

import no.omtheorem.radio.tracks.TrackEntity
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "shows")
data class ShowEntity (
        var name: String = "",
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        var date: LocalDate = LocalDate.of(1970, 1, 1),
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1,
        @OneToMany(cascade = [CascadeType.ALL])
        var tracks: List<TrackEntity> = emptyList()
)
