package mobi.cwiklinski.bloodline.common.manager

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.common.Job
import mobi.cwiklinski.bloodline.common.event.ScreenRoute
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.data.api.TokenService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppManager : KoinComponent {

    const val NOTIFICATION_KEY_ID = "id"

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
                if (data.isNotEmpty()) {
                    if (data.keys.contains(NOTIFICATION_KEY_ID)) {
                        val callbackManager: CallbackManager by inject()
                        when (data[NOTIFICATION_KEY_ID]) {
                            Job.TASK_NOTIFICATION -> {
                                callbackManager.postSideEffect(SideEffects.Redirect(route = ScreenRoute.UnreadNotification))
                            }

                            Job.TASK_DONATION -> {
                                callbackManager.postSideEffect(SideEffects.Redirect(route = ScreenRoute.Donations))
                            }
                        }
                    }
                }
                Logger.d("Notification clicked, Notification payloadData: $data")
            }
        })
    }

}