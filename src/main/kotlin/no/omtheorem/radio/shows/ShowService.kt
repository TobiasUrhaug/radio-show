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

    fun findById(id: Long): ShowForm {
        val showEntity = showRepository.findById(id)
        if (!showEntity.isPresent) { throw ShowNotFoundException("Show with id $id was not found") }
        return ShowForm(showEntity.get())
    }

    fun updateShowDetails(show: ShowForm): ShowForm {
        val existingEntity = showRepository.findById(show.id)
        if (!existingEntity.isPresent) { throw ShowNotFoundException("Show with id $show.id was not found") }
        val updatedEntity: ShowEntity = existingEntity.get().copy()
        updatedEntity.name = show.name
        updatedEntity.date = show.localDate()
        return ShowForm(showRepository.save(updatedEntity))
    }
}
