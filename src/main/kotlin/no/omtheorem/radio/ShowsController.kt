package no.omtheorem.radio

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

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
    fun createShow(@ModelAttribute showForm: ShowForm, model: Model): String {
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

    @GetMapping("/shows/update/{id}")
    fun showUpdateShowForm(@PathVariable id: Long, model: Model): String {
        val show = showRepository.findById(id).get()
        model.addAttribute("show", ShowForm(show.name, show.date.toString(), id))
        return "shows/update"
    }

    @PostMapping("/shows/update/{id}")
    fun updateShow(@PathVariable id: Long, @ModelAttribute showForm: ShowForm): String {
        showRepository.save(ShowEntity(showForm.name, showForm.localDate(), id))
        return "redirect:/"
    }

}