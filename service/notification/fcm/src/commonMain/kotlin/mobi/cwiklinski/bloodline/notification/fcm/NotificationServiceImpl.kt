package mobi.cwiklinski.bloodline.notification.fcm

import com.mmk.kmpnotifier.notification.NotifierManager
import mobi.cwiklinski.bloodline.notification.api.NotificationService
import org.koin.core.component.KoinComponent

open class NotificationServiceImpl : NotificationService, KoinComponent {

    override fun initialize() { }

    override fun sendNotification(id: Int, title: String, body: String) =
        NotifierManager.getLocalNotifier().notify(id, title, body)

    override fun removeNotification(id: Int) = NotifierManager.getLocalNotifier().remove(id)

    override fun removeAllNotifications() = NotifierManager.getLocalNotifier().removeAll()

}