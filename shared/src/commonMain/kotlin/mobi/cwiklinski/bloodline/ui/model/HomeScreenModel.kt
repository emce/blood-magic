package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.ui.manager.CallbackManager
import mobi.cwiklinski.bloodline.ui.util.fillWithRead
import mobi.cwiklinski.bloodline.ui.util.getReadList

class HomeScreenModel(
    callbackManager: CallbackManager,
    profileService: ProfileService,
    donationService: DonationService,
    notificationService: NotificationService,
    storageService: StorageService,
) : AppModel<HomeState>(HomeState.Idle, callbackManager) {

    val donations: StateFlow<List<Donation>> = donationService.getDonations()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _notificationsRead = MutableStateFlow<List<String>>(emptyList())

    val notifications = combine(
            notificationService.getNotifications(),
            _notificationsRead, { notifications, readList ->
                notifications.fillWithRead(readList)
            }
        )

    val profile = profileService.getProfile().stateIn(screenModelScope, SharingStarted.Lazily, Profile(""))

    init {
        bootstrap()
        screenModelScope.launch {
            profileService.getProfile().collectLatest {
                Napier.d(it.toString())
                storageService.storeProfile(it)
            }
            _notificationsRead.emit(storageService.getReadList())
        }
    }
}

sealed class HomeState {
    data object Idle : HomeState()
}