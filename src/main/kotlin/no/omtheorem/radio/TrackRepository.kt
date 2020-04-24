package no.omtheorem.radio

import org.springframework.data.repository.CrudRepository

interface TrackRepository : CrudRepository<TrackEntity, Long>
