package mobi.cwiklinski.bloodline.notification.fcm

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import mobi.cwiklinski.bloodline.notification.api.DesktopNotificationService

class DesktopNotificationServiceImpl : NotificationServiceImpl(), DesktopNotificationService {

    override fun initialize() {
        throw RuntimeException("Use DesktopNotificationServiceImpl::initialize(notificationIconPath: String)")
    }

    override fun initialize(notificationIconPath: String) {
        NotifierManager.initialize(
            NotificationPlatformConfiguration.Desktop(
                showPushNotification = true,
                notificationIconPath = notificationIconPath
            )
        )
    }
}