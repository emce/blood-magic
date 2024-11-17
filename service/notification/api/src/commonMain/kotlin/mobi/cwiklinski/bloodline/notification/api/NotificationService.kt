package mobi.cwiklinski.bloodline.notification.api

import kotlinx.coroutines.flow.Flow

interface NotificationService {

    fun initialize()

    fun sendNotification(id: Int = 1, title: String, body: String)

    fun removeNotification(id: Int)

    fun removeAllNotifications()

}