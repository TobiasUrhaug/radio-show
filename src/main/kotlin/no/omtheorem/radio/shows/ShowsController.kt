package no.omtheorem.radio.shows

import no.omtheorem.radio.tracks.TrackEntity
import no.omtheorem.radio.tracks.TrackForm
import no.omtheorem.radio.tracks.TracklistForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
class ShowsController (){

    @Autowired
    private lateinit var showRepository: ShowRepository

    @GetMapping("/shows/create")
    fun showCreateShowForm(model: Model): String {
        model.addAttribute("show", ShowForm())
        return "shows/create"
    }

    @PostMapping("/shows/create")
    fun createShow(@Valid @ModelAttribute("show") showForm: ShowForm, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            return "shows/create"
        }
        showRepository.save(ShowEntity(showForm.name, showForm.localDate()))
        return "redirect:/"
    }

    @GetMapping("/")
    fun listShows(model: Model): String {
        model.addAttribute("shows", showRepository.findAll().sortedByDescending { it.id })
        return "index"
    }

    @GetMapping("/shows/delete/{id}")
    fun deleteById(@PathVariable id: Long, model: Model): String {
        showRepository.deleteById(id)
        return "redirect:/"
    }

    @GetMapping("/shows/{showId}/update")
    fun showUpdateShowForm(@PathVariable showId: Long, model: Model): String {
        val showEntity = showRepository.findById(showId).get()
        model.addAttribute("show", ShowForm(showEntity))
        model.addAttribute("showId", showId)
        return "shows/update"
    }

    @PostMapping("/shows/{showId}/update")
    fun updateShow(@PathVariable showId: Long, @ModelAttribute showForm: ShowForm): String {
        val existingShow = showRepository.findById(showId).get()
        showRepository.save(ShowEntity(showForm.name, showForm.localDate(), showId, existingShow.tracks))
        return "redirect:/shows/$showId"
    }

    @GetMapping("/shows/{showId}")
    fun getDetails(@PathVariable showId: Long, model: Model): String {
        val showEntity = showRepository.findById(showId).get()
        model.addAttribute("show", ShowForm(showEntity))
        model.addAttribute("showId", showId)
        return "shows/show"
    }

    @GetMapping("/shows/{showId}/tracks/create")
    fun showCreateTracklistForm(@PathVariable showId: Long, model: Model): String {
        model.addAttribute("tracklist", TracklistForm(arrayListOf<TrackForm>()))
        model.addAttribute("showId", showId)
        return "shows/tracklist/new"
    }

    @PostMapping("/shows/{showId}/tracks")
    fun createTracks(@PathVariable showId: Long, @ModelAttribute(value = "tracks") tracklistForm: TracklistForm): String {
        val show = showRepository.findById(showId).get()
        val tracksOnTheShow = show.tracks.toMutableList()
        val addedTracks = tracklistForm.tracks.map { it -> TrackEntity(it.artist, it.title, url = it.url) }
        tracksOnTheShow.addAll(addedTracks)
        show.tracks = tracksOnTheShow
        showRepository.save(show)
        return "redirect:/shows/$showId"
    }

}