package no.omtheorem.radio

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ShowsController {

    @GetMapping("/shows/add")
    fun showAddShowForm(show: Show): String {
        return "add-show"
    }

}