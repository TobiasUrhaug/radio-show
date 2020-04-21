package no.omtheorem.radio

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

    @GetMapping("/shows/details/{id}")
    fun getDetails(@PathVariable id: Long, model: Model): String {
        val showEntity = showRepository.findById(id).get()
        model.addAttribute("show", ShowForm(showEntity.name, showEntity.date.toString(), showEntity.id))
        return "shows/details"
    }

}