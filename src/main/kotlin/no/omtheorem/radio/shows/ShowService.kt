package no.omtheorem.radio.shows

import org.springframework.stereotype.Service

@Service
class ShowService(val showRepository: ShowRepository) {

    fun createShow(show: ShowForm) {
        showRepository.save(ShowEntity(show.name, show.localDate()))
    }

}
