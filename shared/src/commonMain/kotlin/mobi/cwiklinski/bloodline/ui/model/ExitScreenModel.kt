package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.storage.api.StorageService
import kotlin.time.Duration.Companion.seconds

class ExitScreenModel(
    callbackManager: CallbackManager,
    private val authService: AuthenticationService,
    private val donationService: DonationService,
    private val profileService: ProfileService,
    private val storageService: StorageService
) : AppModel<ExitState>(ExitState.Idle, callbackManager) {

    init {
        bootstrap()
        screenModelScope.launch {
            authService.authenticationState
                .collectLatest {
                    if (it == AuthenticationState.NotLogged) {
                        storageService.clearAll()
                        delay(2.seconds.inWholeMilliseconds)
                        mutableState.value = ExitState.LoggedOut
                    }
                }
        }
    }

    fun logout() {
        screenModelScope.launch {
            delay(1.seconds.inWholeMilliseconds)
            authService.logOut().collectLatest {
                if (it) {
                    storageService.clearAll()
                }
            }
        }
    }

    fun delete() {
        screenModelScope.launch {
            donationService.deleteData().collectLatest { result ->
                when (result) {
                    is Either.Left -> {
                        if (result.value) {
                            mutableState.value = ExitState.DonationsDeleted
                            delay(1.seconds.inWholeMilliseconds)
                            profileService.deleteProfile().collectLatest { profileResult ->
                                when (profileResult) {
                                    is Either.Left -> {
                                        if (profileResult.value) {
                                            mutableState.value = ExitState.ProfileDeleted
                                            delay(1.seconds.inWholeMilliseconds)
                                            authService.removeAccount().collectLatest { accountResult ->
                                                storageService.clearAll()
                                                authService.logOut()
                                                if (accountResult) {
                                                    mutableState.value = ExitState.Deleted
                                                } else {
                                                    mutableState.value = ExitState.Error
                                                }
                                            }
                                        } else {
                                            mutableState.value = ExitState.Error
                                        }
                                    }
                                    is Either.Right -> {
                                        mutableState.value = ExitState.Error
                                    }
                                }
                            }
                        } else {
                            mutableState.value = ExitState.Error
                        }
                    }
                    is Either.Right -> {
                        mutableState.value = ExitState.Error
                    }
                }
            }
        }
    }

}

sealed class ExitState {
    data object Idle : ExitState()
    data object LoggedOut: ExitState()
    data object DonationsDeleted : ExitState()
    data object ProfileDeleted : ExitState()
    data object Deleted : ExitState()
    data object Error : ExitState()
}