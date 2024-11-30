package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileServiceState
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.ui.util.Avatar

class SetupScreenModel(
    private val profileService: ProfileService,
    centerService: CenterService,
    private val storageService: StorageService
) : AppModel<SetupState>(SetupState.Idle) {

    val profile = profileService.getProfile()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    val centers: StateFlow<List<Center>> = centerService.getCenters()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        bootstrap()
        screenModelScope.launch {
            profileService.getProfile().collectLatest {
                if (it.name.isEmpty() || it.email.isEmpty()) {
                    mutableState.value = SetupState.NeedSetup
                } else {
                    _email.value = it.email
                    mutableState.value = SetupState.AlreadySetup
                }
                if (it.email.isEmpty()) {
                    _email.value = storageService.getString(Constants.EMAIL_KEY, it.email)
                }
            }
        }
    }



    fun onSetup(
        newName: String,
        newEmail: String,
        newAvatar: Avatar,
        newSex: Sex,
        newNotification: Boolean,
        newStarting: Int,
        newCenter: Center?
    ) {
        mutableState.value = SetupState.SavingData
        screenModelScope.launch {
            profileService.updateProfileData(
                newName, newEmail, newAvatar.name, newSex,
                newNotification, newStarting, newCenter?.id ?: ""
            )
                .collectLatest {
                    when (it) {
                        is ProfileServiceState.Error -> mutableState.value =
                            SetupState.Error(SetupError.ERROR)
                        ProfileServiceState.Idle -> {}
                        ProfileServiceState.Saved -> mutableState.value = SetupState.SavedData
                        ProfileServiceState.Saving -> mutableState.value = SetupState.SavingData
                    }
                }
        }
    }

}

sealed class SetupState {
    data object Idle: SetupState()
    data object NeedSetup: SetupState()
    data object AlreadySetup: SetupState()
    data object SavingData: SetupState()
    data object SavedData: SetupState()
    data class Error(val error: SetupError): SetupState()
}

enum class SetupError {
    ERROR,
    NAME,
    EMAIL,
    CENTER,
}