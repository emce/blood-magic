package mobi.cwiklinski.bloodline.common

import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.common.manager.AppManager
import mobi.cwiklinski.bloodline.common.manager.BackgroundJobManager
import mobi.cwiklinski.bloodline.common.manager.WorkConstraints
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.homeNextDonationReadySubtitle
import mobi.cwiklinski.bloodline.resources.homeNextDonationReadyTitle
import mobi.cwiklinski.bloodline.resources.notificationsUnreadMessage
import mobi.cwiklinski.bloodline.resources.notificationsUnreadTitle
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.ui.util.NextDonationTime
import mobi.cwiklinski.bloodline.ui.util.getLatestRead
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.hours

object Job : KoinComponent {

    const val TASK_NOTIFICATION = "job_notification"
    const val TASK_DONATION = "job_donation"
    private const val ID_NOTIFICATION = 233002
    private const val ID_DONATION = 233003

    fun runNotificationCheck() {
        val coroutineScope: CoroutineScope by inject()
        coroutineScope.launch {
            val auth: AuthenticationService by inject()
            auth.authenticationState.collectLatest {
                val profileService: ProfileService by inject()
                profileService.getProfile().collectLatest {
                    if (it.notification) {
                        val jobManager: BackgroundJobManager by inject()
                        jobManager.cancelTask(TASK_NOTIFICATION)
                        jobManager.enqueuePeriodicWork(
                            taskId = TASK_NOTIFICATION,
                            interval = 24.hours.inWholeMilliseconds,
                            initialDelay = countDelayByHour(9),
                            constraints = WorkConstraints.getDefault(),
                            task = ::checkNotifications
                        )
                    }
                }
            }
        }
    }

    suspend fun checkNotifications() {
        val notificationService: NotificationService by inject()
        val storageService: StorageService by inject()
        val latestRead = storageService.getLatestRead()
        val notifications = notificationService.getNotifications().first()
        val unread = notifications.filter { !it.date.isAfter(latestRead) }
        if (unread.isNotEmpty()) {
            val notifier = NotifierManager.getLocalNotifier()
            notifier.notify(
                id = ID_NOTIFICATION,
                title = getString(Res.string.notificationsUnreadTitle),
                body = getPluralString(Res.plurals.notificationsUnreadMessage, unread.size, unread.size),
                payloadData = mapOf(
                    AppManager.NOTIFICATION_KEY_ID to TASK_NOTIFICATION
                )
            )
        }
    }

    fun runPotentialDonationCheck() {
        val coroutineScope: CoroutineScope by inject()
        coroutineScope.launch {
            val auth: AuthenticationService by inject()
            auth.authenticationState.collectLatest {
                val profileService: ProfileService by inject()
                profileService.getProfile().collectLatest {
                    val jobManager: BackgroundJobManager by inject()
                    jobManager.cancelTask(TASK_DONATION)
                    jobManager.enqueuePeriodicWork(
                        taskId = TASK_DONATION,
                        interval = 24.hours.inWholeMilliseconds,
                        initialDelay = countDelayByHour(7),
                        constraints = WorkConstraints.getDefault(),
                        task = ::checkPotentialDonation
                    )
                }
            }
        }
    }

    suspend fun checkPotentialDonation() {
        val donationService: DonationService by inject()
        val readySubtitle = getString(Res.string.homeNextDonationReadySubtitle)
        val readyTitle = getString(Res.string.homeNextDonationReadyTitle)
        donationService.getDonations().first().firstOrNull()?.let { lastDonation ->
            NextDonationTime(lastDonation, today()).fillData { nextDonationDate ->
                if (nextDonationDate.fullBlood < 1) {
                    val notifier = NotifierManager.getLocalNotifier()
                    notifier.notify(
                        id = ID_DONATION,
                        title = readyTitle,
                        body = readySubtitle,
                        payloadData = mapOf(
                            AppManager.NOTIFICATION_KEY_ID to TASK_DONATION
                        )
                    )
                }
            }
        }
    }
}