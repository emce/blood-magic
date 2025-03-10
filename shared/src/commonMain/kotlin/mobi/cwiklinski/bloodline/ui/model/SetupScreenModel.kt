package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileServiceState
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.ui.util.Avatar

class SetupScreenModel(
    callbackManager: CallbackManager,
    private val profileService: ProfileService,
    centerService: CenterService,
    private val storageService: StorageService
) : AppModel<SetupState>(SetupState.Loading, callbackManager) {

    private val _profile = MutableStateFlow(Profile(""))
    val profile = _profile.asStateFlow()
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _centers = MutableStateFlow<List<Center>>(emptyList())
    val centers = _centers.asStateFlow()

    init {
        bootstrap()
        screenModelScope.launch {
            val currentProfile = profileService.getProfile().first()
            _profile.value = currentProfile
            if (currentProfile.name.isEmpty()) {
                mutableState.value = SetupState.NeedSetup
            } else {
                mutableState.value = SetupState.AlreadySetup
            }
            _centers.value = centerService.getCenters().first()
            _email.value = storageService.getString(Constants.EMAIL_KEY, "")
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

    fun resetState() {
        mutableState.value = SetupState.Idle
    }
}

sealed class SetupState {
    data object Loading: SetupState()
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