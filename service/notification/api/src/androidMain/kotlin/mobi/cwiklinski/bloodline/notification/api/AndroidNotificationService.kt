package mobi.cwiklinski.bloodline.notification.api

import android.app.Activity
import android.content.Intent

interface AndroidNotificationService : NotificationService {

    fun initialize(notificationIcon: Int)

    fun onCreateOrOnNewIntent(intent: Intent)

    fun addNotificationListener(listener: NotificationListener)

}

interface NotificationListener {

    fun onPushNotification(title:String?,body:String?)

    fun onPayloadData(data: Map<String,*>)

    fun onNotificationClicked(data: Map<String,*>)

}