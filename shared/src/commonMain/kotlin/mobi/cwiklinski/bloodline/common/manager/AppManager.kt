package mobi.cwiklinski.bloodline.common.manager

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.data.api.TokenService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppManager : KoinComponent {

    fun onApplicationStart() {
        NotifierManager.addListener(object : NotifierManager.Listener {

            override fun onNewToken(token: String) {
                Logger.d("Push Notification onNewToken: $token")
                val tokenService: TokenService by inject()
                val scope: CoroutineScope by inject()
                scope.launch {
                    tokenService.addToken(token).collectLatest {
                        Logger.d("Saved token: $it")
                    }
                }
            }

            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                val notifier = NotifierManager.getLocalNotifier()
                title?.let { notificationTitle ->
                    body?.let { notificationBody ->
                        notifier.notify(notificationTitle, notificationBody)
                    }
                }
            }

            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                Logger.d("Push Notification payloadData: $data")
            }

            override fun onNotificationClicked(data: PayloadData) {
                super.onNotificationClicked(data)
                Logger.d("Notification clicked, Notification payloadData: $data")
            }
        })
    }

}