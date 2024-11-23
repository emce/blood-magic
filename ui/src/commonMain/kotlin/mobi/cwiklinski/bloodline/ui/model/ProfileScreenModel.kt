package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.storage.api.StorageService

class ProfileScreenModel(
    private val profileService: ProfileService,
    private val authService: AuthenticationService,
    private val storageService: StorageService
) : AppModel<ProfileState>(ProfileState.Idle) {

    val profile = profileService.getProfile()

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
        if (newName != currentProfile.name || currentProfile.differs(
                newAvatar,
                newSex,
                newNotification,
                newStarting,
                newCenterId
            )
        ) {
            screenModelScope.launch {
                profileService.updateProfileData(
                    newName, newAvatar, newSex,
                    newNotification, newStarting, newCenterId
                )
                    .collectLatest {
                        when (it) {
                            is Either.Left -> {
                                profile.value.let { newProfile ->
                                    storageService.storeProfile(newProfile)
                                }
                                mutableState.value = ProfileState.Saved
                            }

                            is Either.Right -> {
                                mutableState.value =
                                    ProfileState.Error(listOf(ProfileError.ERROR))
                            }
                        }
                    }
            }
        }
    }

    fun onProfilePasswordUpdate(currentPassword: String, newPassword: String, repeat: String) {
        mutableState.value = ProfileState.Saving
        val currentProfile = profile.value
        if (newPassword == repeat) {
            if (currentPassword.isNotEmpty() && currentPassword.length > 5) {
                screenModelScope.launch {
                    val logged =
                        authService.loginWithEmailAndPassword(
                            currentProfile.email,
                            currentPassword
                        )
                    if (logged.first() is AuthResult.Success) {
                        if (newPassword.isNotEmpty() && newPassword.length > 5) {
                            profileService.updateProfilePassword(newPassword)
                                .collectLatest {
                                    when (it) {
                                        is Either.Left -> mutableState.value =
                                            ProfileState.Saved

                                        is Either.Right -> mutableState.value =
                                            ProfileState.Error(listOf(ProfileError.PASSWORD))
                                    }
                                }
                        } else {
                            mutableState.value =
                                ProfileState.Error(listOf(ProfileError.NEW_PASSWORD))
                        }
                    } else {
                        mutableState.value =
                            ProfileState.Error(listOf(ProfileError.CURRENT_PASSWORD))
                    }
                }
            } else {
                mutableState.value = ProfileState.Error(listOf(ProfileError.CURRENT_PASSWORD))
            }
        } else {
            mutableState.value = ProfileState.Error(listOf(ProfileError.REPEAT))
        }
    }

    fun onProfileEmailUpdate(newEmail: String) {
        mutableState.value = ProfileState.Saving
        val currentProfile = profile.value
        if (newEmail.isValidEmail() && currentProfile.email != newEmail) {
            screenModelScope.launch {
                profileService.updateProfileEmail(newEmail)
                    .collectLatest {
                        when (it) {
                            is Either.Left -> mutableState.value = ProfileState.Saved
                            is Either.Right -> mutableState.value =
                                ProfileState.Error(listOf(ProfileError.EMAIL))
                        }
                    }
            }
        } else {
            mutableState.value = ProfileState.Error(listOf(ProfileError.NEW_EMAIL))
        }
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
    NEW_PASSWORD,
    PASSWORD,
    REPEAT,
    EMAIL,
    NEW_EMAIL,
    DATA,
    ERROR

}