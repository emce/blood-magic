package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.model.Notification

@Serializable
data class FirebaseNotification(
    val id: String,
    val type: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val message: String,
    val location: String,
) {
    fun toNotification() = Notification(
        id = this.id,
        type = this.type,
        date = LocalDate(this.year, this.month, this.day),
        title = this.title,
        message = this.message,
        location = this.location
    )
}