package no.omtheorem.radio

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ShowEntity (
        val name: String = "",
        val date: String = "",
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1
)
