package mobi.cwiklinski.bloodline.notification.fcm

import com.mmk.kmpnotifier.notification.NotifierManager
import mobi.cwiklinski.bloodline.notification.api.NotificationService
import org.koin.core.component.KoinComponent

open class NotificationServiceImpl : NotificationService, KoinComponent {

    protected val notifier = NotifierManager.getLocalNotifier()

    override fun initialize() {}

    override fun sendNotification(id: Int, title: String, body: String) =
        notifier.notify(id, title, body)

    override fun removeNotification(id: Int) = notifier.remove(id)

    override fun removeAllNotifications() = notifier.removeAll()

}