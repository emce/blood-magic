package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileServiceState
import mobi.cwiklinski.bloodline.data.filed.withEmail
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.storage.api.StorageService
import kotlin.time.Duration.Companion.seconds

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
                var email = profile.email
                if (email.isEmpty()) {
                    email = storageService.getString(Constants.EMAIL_KEY, email)
                }
                Napier.d(profile.toString())
                storageService.storeProfile(profile.withEmail(email))
            }
        }
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
                        ProfileServiceState.Saved -> mutableState.value = ProfileState.Saved
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
                                    .timeout(10.seconds)
                                    .catch {
                                        ProfileState.Error(listOf(ProfileError.PASSWORD))
                                    }
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
                            is Either.Left -> mutableState.value = ProfileState.Saved
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
        screenModelScope.launch {
            authService.logOut()
                .map {
                    delay(1.seconds.inWholeMilliseconds)
                    if (it) {
                        storageService.clearAll()
                        authService.logOut()
                    }
                    it
                }
                .collectLatest {
                    delay(2.seconds.inWholeMilliseconds)
                    mutableState.value = if (it)
                        ProfileState.LoggedOut
                    else
                        ProfileState.Idle
                }
        }
    }
}

sealed class ProfileState {

    data object Idle : ProfileState()

    data object Saving : ProfileState()

    data object Saved : ProfileState()

    data class Error(val errors: List<ProfileError>) : ProfileState()

    data object ToLoggedOut : ProfileState()

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