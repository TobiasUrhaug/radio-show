package no.omtheorem.radio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RadioApplication

fun main(args: Array<String>) {
	runApplication<RadioApplication>(*args)
}
