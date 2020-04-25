package no.omtheorem.radio.tracks

import no.omtheorem.radio.tracks.TrackEntity
import org.springframework.data.repository.CrudRepository

interface TrackRepository : CrudRepository<TrackEntity, Long>
