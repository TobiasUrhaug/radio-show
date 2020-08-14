package no.omtheorem.radio.shows

import org.springframework.stereotype.Service

@Service
class ShowService(val showRepository: ShowRepository) {

    fun createShow(show: ShowForm) {
        showRepository.save(ShowEntity(show.name, show.localDate()))
    }

    fun listShows(): List<ShowForm> {
        return showRepository
                .findAll()
                .sortedByDescending { it.id }
                .map { ShowForm(it) }
    }

    fun deleteShow(id: Long) {
        return showRepository.deleteById(id)
    }

}
