package mobi.cwiklinski.bloodline.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String,
    val type: Int,
    val date: LocalDate,
    val title: String,
    val message: String,
    val location: String,
    val read: Boolean = false
)