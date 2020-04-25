package no.omtheorem.radio.shows

import no.omtheorem.radio.tracks.TrackForm
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.validation.constraints.NotEmpty

class ShowForm(
        @get:NotEmpty(message = "Name cannot be empty")
        val name: String = "",
        @get:NotEmpty(message = "Please select a date")
        val date: String = "",
        val id: Long = -1,
        val tracks: List<TrackForm> = emptyList()
) {

    constructor(showEntity: ShowEntity) : this(
            name = showEntity.name,
            date = showEntity.date.toString(),
            id = showEntity.id,
            tracks = showEntity.tracks.map { it -> TrackForm(it.artist, it.name) }
    ) {
    }

    fun localDate(): LocalDate {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShowForm

        if (name != other.name) return false
        if (date != other.date) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}