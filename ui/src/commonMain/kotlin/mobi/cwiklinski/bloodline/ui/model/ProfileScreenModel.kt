package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.storage.api.StorageService

class ProfileScreenModel(
    private val profileService: ProfileService,
    private val storageService: StorageService
) : AppModel<ProfileState>(ProfileState.Idle) {

    val profile = profileService.getProfile()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        bootstrap()
        screenModelScope.launch {
            profileService.getProfile().collectLatest {
                storageService.storeProfile(it)
            }
        }
    }

    fun onProfileDataUpdate(
        newName: String, newAvatar: String, newSex: Sex, newNotification: Boolean,
        newStarting: Int, newCenterId: String
    ) {
        mutableState.value = ProfileState.Saving
        val currentProfile = profile.value
        if (currentProfile != null) {
            if (newName != currentProfile.name || currentProfile.differs(
                    newAvatar,
                    newSex,
                    newNotification,
                    newStarting,
                    newCenterId
                )
            ) {
                screenModelScope.launch {
                    profileService.updateProfileData(newName, newAvatar, newSex,
                        newNotification, newStarting, newCenterId)
                        .collectLatest {
                            when (it) {
                                is Either.Left -> {

                                }
                                is Either.Right -> {
                                    mutableState.value = ProfileState.Error(listOf(ProfileError.ERROR))
                                }
                            }
                        }
                }
            }
        } else {
            mutableState.value = ProfileState.Error(listOf(ProfileError.ERROR))
        }
    }

    fun onProfilePasswordUpdate(currentPassword: String, newPassword: String, repeat: String) {

    }

    fun onProfileEmailUpdate(newEmail: String) {

    }
}

sealed class ProfileState {

    data object Idle : ProfileState()

    data object Saving : ProfileState()

    data object Saved : ProfileState()

    data class Error(val errors: List<ProfileError>) : ProfileState()

}

enum class ProfileError {
    CURRENT_PASSWORD,
    PASSWORD,
    REPEAT,
    EMAIL,
    DATA,
    ERROR

}