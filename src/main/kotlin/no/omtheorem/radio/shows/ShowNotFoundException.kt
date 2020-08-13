package no.omtheorem.radio.shows

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class ShowNotFoundException : RuntimeException {
    constructor(message: String, ex: Exception?): super(message, ex) {}
    constructor(message: String): super(message) {}
    constructor(ex: Exception): super(ex) {}
}