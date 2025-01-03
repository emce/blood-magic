package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseNotification

class NotificationServiceImplementation(db: FirebaseDatabase) : NotificationService {

    private val mainRef = db.reference("notification")

    override fun getNotifications() = mainRef
        .valueEvents
        .map { events ->
            events.value<Map<String, FirebaseNotification>>().values.map { it.toNotification() }
                .toList().sortedByDescending { it.date }
        }
}