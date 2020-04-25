package no.omtheorem.radio.shows

import org.springframework.data.repository.CrudRepository

interface ShowRepository : CrudRepository<ShowEntity, Long>
