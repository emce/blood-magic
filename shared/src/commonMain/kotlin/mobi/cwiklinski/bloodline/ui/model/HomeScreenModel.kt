package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.ui.util.getLatestRead
import mobi.cwiklinski.bloodline.ui.util.setLatestRead
import kotlin.time.Duration.Companion.seconds

class HomeScreenModel(
    callbackManager: CallbackManager,
    profileService: ProfileService,
    donationService: DonationService,
    notificationService: NotificationService,
    storageService: StorageService,
) : AppModel<HomeState>(HomeState.Idle, callbackManager) {

    val donations = donationService.getDonations()
    val notifications = MutableStateFlow(emptyList<Notification>())
    private val _profile = MutableStateFlow(Profile(""))
    val profile = _profile.asStateFlow()

    init {
        bootstrap()
        screenModelScope.launch {
            val currentProfile = profileService.getProfile().first()
            Logger.d(currentProfile.toString())
            _profile.value = currentProfile
            storageService.storeProfile(currentProfile)
            notificationService.getNotifications().collectLatest { notificationList ->
                val latestRead = storageService.getLatestRead()
                notifications.emit(notificationList.filter { it.date.isAfter(latestRead) })
            }
            delay(3.seconds.inWholeMilliseconds)
            storageService.setLatestRead()
        }
    }
}

sealed class HomeState {
    data object Idle : HomeState()
}