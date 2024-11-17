package mobi.cwiklinski.bloodline.notification.api

interface  DesktopNotificationService : NotificationService {

    fun initialize(notificationIconPath: String)

}