package no.omtheorem.radio

import javax.persistence.*

@Entity
@Table(name = "tracks")
data class TrackEntity(
        val artist: String,
        val name: String,
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1
)
