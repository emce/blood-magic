package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileServiceState
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.common.manager.CallbackManager

class ProfileScreenModel(
    callbackManager: CallbackManager,
    private val profileService: ProfileService,
    centerService: CenterService,
    private val authService: AuthenticationService,
    private val storageService: StorageService
) : AppModel<ProfileState>(ProfileState.Idle, callbackManager) {

    private val _profile = MutableStateFlow(Profile(""))
    val profile = _profile.asStateFlow()

    val centers = centerService.getCenters()

    init {
        bootstrap()
        updateProfile()
    }

    fun onProfileDataUpdate(
        newName: String, newEmail: String, newAvatar: String, newSex: Sex, newNotification: Boolean,
        newStarting: Int, newCenterId: String
    ) {
        mutableState.value = ProfileState.Saving
        screenModelScope.launch {
            profileService.updateProfileData(
                newName, newEmail, newAvatar, newSex,
                newNotification, newStarting, newCenterId
            )
                .collectLatest {
                    when (it) {
                        is ProfileServiceState.Error -> mutableState.value = ProfileState.Error(listOf(ProfileError.ERROR))
                        ProfileServiceState.Idle -> {}
                        ProfileServiceState.Saved -> {
                            mutableState.value = ProfileState.Saved
                            updateProfile()
                        }
                        ProfileServiceState.Saving -> {}
                    }
                }
        }
    }

    fun onProfilePasswordUpdate(currentPassword: String, newPassword: String, repeat: String) {
        mutableState.value = ProfileState.Saving
        screenModelScope.launch {
            profile.collectLatest { currentProfile ->
                if (newPassword == repeat) {
                    if (currentPassword.isNotEmpty() && currentPassword.length > 5) {
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
                                            is Either.Left -> {
                                                mutableState.value = ProfileState.Saved
                                                updateProfile()
                                            }

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
                    } else {
                        mutableState.value =
                            ProfileState.Error(listOf(ProfileError.CURRENT_PASSWORD))
                    }
                } else {
                    mutableState.value = ProfileState.Error(listOf(ProfileError.REPEAT))
                }
            }
        }
    }

    fun onProfileEmailUpdate(newEmail: String) {
        mutableState.value = ProfileState.Saving
        screenModelScope.launch {
            if (newEmail.isValidEmail()) {
                profileService.updateProfileEmail(newEmail)
                    .collectLatest {
                        when (it) {
                            is Either.Left -> {
                                mutableState.value = ProfileState.Saved
                                updateProfile()
                            }
                            is Either.Right -> mutableState.value =
                                ProfileState.Error(listOf(ProfileError.EMAIL))
                        }
                    }
            } else {
                mutableState.value = ProfileState.Error(listOf(ProfileError.NEW_EMAIL))
            }
        }
    }

    fun loggingOut() {
        mutableState.value = ProfileState.ToLoggedOut
    }

    fun cancelLogout() {
        mutableState.value = ProfileState.Idle
    }

    fun logout() {
        mutableState.value = ProfileState.LoggingOut
    }

    fun setToDelete() {
        mutableState.value = ProfileState.ToDelete
    }

    fun resetState() {
        mutableState.value = ProfileState.Idle
    }

    private fun updateProfile() {
        screenModelScope.launch {
            val currentProfile = profileService.getProfile().first()
            var email = currentProfile.email
            if (email.isEmpty()) {
                email = storageService.getString(Constants.EMAIL_KEY, email)
                if (email.isNotEmpty() && email.isValidEmail()) {
                    storageService.storeProfile(currentProfile.withEmail(email))
                }
            }
            Logger.d(currentProfile.toString())
            _profile.value = currentProfile
        }
    }
}

sealed class ProfileState {

    data object Idle : ProfileState()

    data object Saving : ProfileState()

    data object Saved : ProfileState()

    data class Error(val errors: List<ProfileError>) : ProfileState()

    data object ToLoggedOut : ProfileState()

    data object ToDelete : ProfileState()

    data object LoggingOut : ProfileState()

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