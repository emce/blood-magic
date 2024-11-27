package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
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

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    init {
        bootstrap()
        screenModelScope.launch {
            storageService.getProfile()?.let {
                Napier.d(it.toString())
                _profile.emit(it)
            }
            profileService.getProfile().collectLatest { it ->
                if (it != null) {
                    Napier.d(it.toString())
                    _profile.emit(it)
                    storageService.storeProfile(it)
                } else {
                    storageService.getProfile()?.let { profile ->
                        Napier.d(profile.toString())
                        _profile.emit(profile)
                    }
                }
            }
        }
    }
}

sealed class HomeState {
    data object Idle : HomeState()
}