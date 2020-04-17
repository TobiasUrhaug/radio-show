package no.omtheorem.radio

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Controller
class ShowsController (){

    @Autowired
    private lateinit var showRepository: ShowRepository

    @GetMapping("/shows/add")
    fun showAddShowForm(model: Model): String {
        model.addAttribute("show", ShowForm())
        return "shows/add"
    }

    @PostMapping("/shows")
    fun addShow(@ModelAttribute show: ShowForm, model: Model): String {
        val date = LocalDate.parse(show.date, DateTimeFormatter.ISO_LOCAL_DATE)
        showRepository.save(ShowEntity(show.name, date))
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
    fun updateShow(@PathVariable id: Long, @ModelAttribute show: ShowForm): String {
        val date = LocalDate.parse(show.date, DateTimeFormatter.ISO_LOCAL_DATE)
        showRepository.save(ShowEntity(show.name, date, id))
        return "redirect:/"
    }

}