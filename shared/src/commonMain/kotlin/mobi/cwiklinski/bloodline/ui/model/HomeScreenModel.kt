package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService


class HomeScreenModel(
    profileService: ProfileService,
    donationService: DonationService,
    storageService: StorageService
) : AppModel<HomeState>(HomeState.Idle) {

    val donations: StateFlow<List<Donation>> = donationService.getDonations()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val profile = profileService.getProfile().stateIn(screenModelScope, SharingStarted.Lazily, Profile(""))

    init {
        bootstrap()
        screenModelScope.launch {
            profileService.getProfile().collectLatest {
                Napier.d(it.toString())
                storageService.storeProfile(it)
            }
        }
    }
}

sealed class HomeState {
    data object Idle : HomeState()
}