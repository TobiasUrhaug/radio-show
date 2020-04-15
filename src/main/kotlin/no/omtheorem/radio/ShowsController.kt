package no.omtheorem.radio

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.persistence.Id

@Controller
class ShowsController (){

    @Autowired
    private lateinit var showRepository: ShowRepository

    @GetMapping("/shows/add")
    fun showAddShowForm(model: Model): String {
        model.addAttribute("show", ShowEntity())
        return "shows/add"
    }

    @PostMapping("/shows")
    fun addShow(@ModelAttribute show: ShowEntity, result: BindingResult, model: Model): String {
        showRepository.save(show)
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

}