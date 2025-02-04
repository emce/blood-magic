package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.ui.util.fillWithRead
import mobi.cwiklinski.bloodline.ui.util.getReadList

class HomeScreenModel(
    callbackManager: CallbackManager,
    profileService: ProfileService,
    donationService: DonationService,
    notificationService: NotificationService,
    storageService: StorageService,
) : AppModel<HomeState>(HomeState.Idle, callbackManager) {

    val donations = donationService.getDonations()
    private val _notificationsRead = MutableStateFlow<List<String>>(emptyList())
    val notifications = combine(
            notificationService.getNotifications(),
            _notificationsRead
    ) { notifications, readList ->
        notifications.fillWithRead(readList)
    }
    private val _profile = MutableStateFlow(Profile(""))
    val profile = _profile.asStateFlow()

    init {
        bootstrap()
        screenModelScope.launch {
            val currentProfile = profileService.getProfile().first()
            Logger.d(currentProfile.toString())
            _profile.value = currentProfile
            storageService.storeProfile(currentProfile)
            _notificationsRead.emit(storageService.getReadList())
        }
    }
}

sealed class HomeState {
    data object Idle : HomeState()
}