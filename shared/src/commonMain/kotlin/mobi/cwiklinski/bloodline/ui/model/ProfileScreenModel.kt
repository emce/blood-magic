package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.common.removeDiacritics
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.storage.api.StorageService

class ProfileScreenModel(
    private val profileService: ProfileService,
    centerService: CenterService,
    private val authService: AuthenticationService,
    private val storageService: StorageService
) : AppModel<ProfileState>(ProfileState.Idle) {

    val profile = profileService.getProfile()

    val centers: StateFlow<List<Center>> = centerService.getCenters()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        bootstrap()
        screenModelScope.launch {
            profileService.getProfile().collectLatest { profile ->
                Napier.d(profile.toString())
                if (profile != null) {
                    storageService.storeProfile(profile)
                }
            }
        }
    }

    fun onProfileDataUpdate(
        newName: String, newAvatar: String, newSex: Sex, newNotification: Boolean,
        newStarting: Int, newCenterId: String
    ) {
        mutableState.value = ProfileState.Saving
        val currentProfile = profile.value
        if (newName != currentProfile?.name || currentProfile.differs(
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
                                profile.collectLatest { newProfile ->
                                    if (newProfile != null) {
                                        storageService.storeProfile(newProfile)
                                        mutableState.value = ProfileState.Saved
                                    } else {
                                        mutableState.value =
                                            ProfileState.Error(listOf(ProfileError.ERROR))
                                    }
                                }
                            }

                            is Either.Right -> {
                                mutableState.value =
                                    ProfileState.Error(listOf(ProfileError.ERROR))
                            }
                        }
                    }
            }
        } else {
            mutableState.value = ProfileState.Error(listOf(ProfileError.ERROR))
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
                            currentProfile?.email ?: "",
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
        if (newEmail.isValidEmail() && currentProfile?.email != newEmail) {
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

    fun loggingOut() {
        mutableState.value = ProfileState.LoggingOut
    }

    fun logout() {
        screenModelScope.launch {
            authService.logOut().debounce(1000).collectLatest {
                if (it) {
                    storageService.clearAll()
                    authService.logOut()
                    mutableState.value = ProfileState.LoggedOut
                } else {
                    mutableState.value = ProfileState.Idle
                }
            }
        }
    }
}

sealed class ProfileState {

    data object Idle : ProfileState()

    data object Saving : ProfileState()

    data object Saved : ProfileState()

    data class Error(val errors: List<ProfileError>) : ProfileState()

    data object LoggingOut : ProfileState()

    data object LoggedOut : ProfileState()

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