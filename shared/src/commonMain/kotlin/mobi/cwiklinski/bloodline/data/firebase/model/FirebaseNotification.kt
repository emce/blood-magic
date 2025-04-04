package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.domain.NotificationType
import mobi.cwiklinski.bloodline.domain.model.Notification

@Serializable
data class FirebaseNotification(
    val id: String = "",
    val type: Int = 1,
    val year: Int = today().year,
    val month: Int = today().monthNumber,
    val day: Int = today().dayOfMonth,
    val title: String = "",
    val message: String = "",
    val location: String = "",
) {
    fun toNotification(): Notification {
        var notification = Notification()
        if (id.isNotEmpty()) {
            notification = notification.copy(id = id)
        }
        notification = notification.copy(type = NotificationType.byType(type))
        notification = notification.copy(date = LocalDate(year, month, day))
        if (title.isNotEmpty()) {
            notification = notification.copy(title = title)
        }
        if (message.isNotEmpty()) {
            notification = notification.copy(message = message)
        }
        if (location.isNotEmpty()) {
            notification = notification.copy(location = location)
        }
        return notification
    }
}