package mobi.cwiklinski.bloodline.notification.fcm

import android.content.Intent
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import mobi.cwiklinski.bloodline.notification.api.AndroidNotificationService
import mobi.cwiklinski.bloodline.notification.api.NotificationListener

class AndroidNotificationServiceImpl() : NotificationServiceImpl(),
    AndroidNotificationService {

    override fun initialize() {
        throw RuntimeException("Use AndroidNotificationServiceImpl::initialize(notificationIcon: Int)")
    }

    override fun initialize(notificationIcon: Int) {
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = notificationIcon,
                showPushNotification = true,
            )
        )
    }

    override fun onCreateOrOnNewIntent(intent: Intent) {
        NotifierManager.onCreateOrOnNewIntent(intent)
    }

    override fun addNotificationListener(listener: NotificationListener) {
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNotificationClicked(data: PayloadData) {
                listener.onNotificationClicked(data)
            }

            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                listener.onPayloadData(data)
            }

            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                listener.onPushNotification(title, body)
            }
        })
    }

}