package no.omtheorem.radio.shows

import no.omtheorem.radio.tracks.TrackEntity
import no.omtheorem.radio.tracks.TrackForm
import no.omtheorem.radio.tracks.TrackRepository
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
    @Autowired
    private lateinit var showService: ShowService
    @Autowired
    private lateinit var trackRepository: TrackRepository

    @GetMapping("/shows/create")
    fun showCreateShowForm(model: Model): String {
        model.addAttribute("show", ShowForm())
        return "shows/create-show"
    }

    @PostMapping("/shows/create")
    fun createShow(@Valid @ModelAttribute("show") showForm: ShowForm, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            return "shows/create-show"
        }
        showService.createShow(showForm)
        return "redirect:/"
    }

    @GetMapping("/")
    fun listShows(model: Model): String {
        model.addAttribute("shows", showService.listShows())
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
        existingShow.name = showForm.name
        existingShow.date = showForm.localDate()
        showRepository.save(existingShow)
        return "redirect:/shows/$showId"
    }

    @GetMapping("/shows/{showId}")
    fun getDetails(@PathVariable showId: Long, model: Model): String {
        val showEntity = showRepository.findById(showId)
        if (!showEntity.isPresent) {
            throw ShowNotFoundException("Show with id $showId was not found");
        }
        model.addAttribute("show", ShowForm(showEntity.get()))
        model.addAttribute("showId", showId)
        return "shows/show"
    }

    @GetMapping("/shows/{showId}/tracks/create")
    fun showManageTracklistForm(@PathVariable showId: Long, model: Model): String {
        val show = showRepository.findById(showId).get()
        val tracks = show.tracks.map { TrackForm(it.artist, it.name, it.url, it.remix, it.label) }
        model.addAttribute("tracklist", TracklistForm(tracks))
        model.addAttribute("showId", showId)
        return "shows/tracklist/manage-tracklist"
    }

    @PostMapping("/shows/{showId}/tracks")
    fun createTracklist(@PathVariable showId: Long, @ModelAttribute(value = "tracks") tracklistForm: TracklistForm): String {
        val show = showRepository.findById(showId).get()
        trackRepository.deleteAll(show.tracks)
        show.tracks = tracklistForm.tracks
                .filter { !it.isEmpty() }
                .map { TrackEntity(it.artist, it.title, url = it.url, remix = it.remix, label = it.label) }
        showRepository.save(show)
        return "redirect:/shows/$showId"
    }

}