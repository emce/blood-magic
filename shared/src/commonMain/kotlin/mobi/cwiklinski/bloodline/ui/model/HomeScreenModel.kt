package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.storage.api.StorageService


class HomeScreenModel(
    profileService: ProfileService,
    donationService: DonationService,
    storageService: StorageService
) : AppModel<HomeState>(HomeState.Idle) {

    val donations: StateFlow<List<Donation>> = donationService.getDonations()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val profile = profileService.getProfile()

    init {
        bootstrap()
        screenModelScope.launch {
            profileService.getProfile().collectLatest {
                if (it != null) {
                    storageService.storeProfile(it)
                }
            }
        }
    }
}

sealed class HomeState {
    data object Idle : HomeState()
}